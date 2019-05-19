package com.noredine69.orge.ws.business.impl;

import com.noredine69.orge.ws.business.BusinessRateService;
import com.noredine69.orge.ws.core.service.ComputeRule;
import com.noredine69.orge.ws.converter.DurationConverter;
import com.noredine69.orge.ws.core.model.FeeRule;
import com.noredine69.orge.ws.core.model.SearchRuleCriteria;
import com.noredine69.orge.ws.core.service.FeeRuleService;
import com.noredine69.orge.ws.geoloc.model.IpLocation;
import com.noredine69.orge.ws.geoloc.service.GeolocServiceImpl;
import com.noredine69.orge.ws.model.FeeDto;
import com.noredine69.orge.ws.model.FeeRequestDto;
import lombok.extern.slf4j.Slf4j;
import net.openhft.compiler.CompilerUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.threeten.bp.temporal.ChronoUnit;

import javax.annotation.PostConstruct;
import java.io.File;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.time.Duration;
import java.util.jar.JarEntry;

@Slf4j
@Service
public class BusinessRateServiceImpl implements BusinessRateService {

    public static final String ORGE_WS_SERVICE = "orge-ws-service";
    public static final String JAR = "jar";
    @Value("${temp.dir}")
    private String tempDir;

    @Autowired
    private GeolocServiceImpl geolocService;

    @Autowired
    private FeeRuleService feeRuleService;

    @PostConstruct
    public void init(){
        setCompilerClassPathWithServiceLibrary();
    }

    private void setCompilerClassPathWithServiceLibrary() {
        final String orgeWsServiceLibraryPath = searchLibraryClasspath();
        log.debug("java.class.path before : " + System.getProperty("java.class.path"));
        CompilerUtils.addClassPath(orgeWsServiceLibraryPath);
        log.debug("java.class.path after : " + System.getProperty("java.class.path"));
    }

    private String searchLibraryClasspath() {
        final String commandLineJarFile = findCommandLineJarFile();
        log.debug("commandLineJarFile : "+commandLineJarFile);
        String filePathInArchive = extractJar(commandLineJarFile, tempDir, ORGE_WS_SERVICE, JAR);
        final String orgeWsServiceLibraryPath = findFileInDirectory(filePathInArchive, tempDir);
        log.debug("orgeWsServiceLibraryPath: "+orgeWsServiceLibraryPath);
        return orgeWsServiceLibraryPath;
    }

    private String findFileInDirectory(final String filePathInArchive, final String directoryPath) {
        File fileInArchive = new File(directoryPath + FileSystems.getDefault().getSeparator() + filePathInArchive);
        log.debug("fileInArchive.getAbsolutePath(): "+fileInArchive.getAbsolutePath());
        if(fileInArchive.exists()) {
            return fileInArchive.getAbsolutePath();
        }
        return null;
    }

    private String findCommandLineJarFile() {
        final String property = System.getProperty("sun.java.command");
        log.debug("property " + property);
        return property;
    }

    private static final String COMPUTE_RULE_CLASSNAME = "com.noredine69.orge.ws.business.impl.ComputeRuleImpl";
    private static final String BASE_JAVA_CODE =
                    "package com.noredine69.orge.ws.business.impl;\n" +
                    "import com.noredine69.orge.ws.core.service.ComputeRule;\n"+
                    "import com.noredine69.orge.ws.core.model.SearchRuleCriteria;\n"+
                    "public class ComputeRuleImpl%d implements ComputeRule {\n" +
                    "    public boolean checkRule(final SearchRuleCriteria searchRuleCriteria) {\n" +
                    "       return (%s);\n"+
                    "    }\n" +
                    "}\n";

    @Override
    public boolean checkRuleOnCriteria(final String criteria, final SearchRuleCriteria searchRuleCriteria, long id) {
        try {
            log.debug("criteria {} searchRuleCriteria {} id {} ", criteria, searchRuleCriteria, id);
            final String javaCode = String.format(BASE_JAVA_CODE, id, criteria);
            log.debug("javaCode {} ", javaCode);
            Class aClass = CompilerUtils.CACHED_COMPILER.loadFromJava(COMPUTE_RULE_CLASSNAME+id, javaCode);
            ComputeRule computeRule = (ComputeRule) aClass.newInstance();
            log.debug("criteria {} searchRuleCriteria {} id {} ", criteria, searchRuleCriteria, id);
            return computeRule.checkRule(searchRuleCriteria);
        }catch (IllegalAccessException  | InstantiationException | ClassNotFoundException e ){
            log.error("Error during executing runtime generated Java code {} {}", criteria, e);
        }finally {
            CompilerUtils.CACHED_COMPILER.close();
        }
        return false;
    }

    private String extractJar(final String jarFile, final String destDir, final String fileStartPattern, final String fileEndPattern) {

        File rootDirFile = new File(destDir);
        if(rootDirFile.exists()){
            log.debug("target directory exists, trying to delete it!");
            rootDirFile.delete();
        }

        log.debug("target directory doesn't exist, trying to create it!");
        rootDirFile.mkdir();

        try(java.util.jar.JarFile jar = new java.util.jar.JarFile(jarFile);) {
            java.util.Enumeration enumEntries = jar.entries();
            while (enumEntries.hasMoreElements()) {
                JarEntry file = (java.util.jar.JarEntry) enumEntries.nextElement();
                File f = new File(destDir + java.io.File.separator + file.getName());
                if (file.isDirectory()) { // if its a directory, create it
                    f.mkdir();
                    continue;
                }
                log.debug("file in jar : "+file.getName()
                        +" "+file.getName().contains(fileStartPattern)
                        +" "+file.getName().endsWith(fileEndPattern)
                );
                if( file.getName().contains(fileStartPattern) && file.getName().endsWith(fileEndPattern) ) {
                    log.debug("file found, trying to extract it !");
                    try(
                            java.io.InputStream is = jar.getInputStream(file); // get the input stream
                            java.io.FileOutputStream fos = new java.io.FileOutputStream(f)) {
                        while (is.available() > 0) {  // write contents of 'is' to 'fos'
                            fos.write(is.read());
                        }
                    }
                    return file.getName();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public FeeDto searchRate(FeeRequestDto feeRequestDto) {
        final IpLocation clientIpLocation = this.geolocService.geolocFromIp(feeRequestDto.getClient().getIp());
        log.debug("client ip location : " + clientIpLocation);
        final IpLocation freelanceIpLocation = this.geolocService.geolocFromIp(feeRequestDto.getFreelancer().getIp());
        log.debug("freelance ip location : " + freelanceIpLocation);
        for(FeeRule feeRule : feeRuleService.findNotDefaultFeeRuleWithLocation(freelanceIpLocation.getCountry_code(), clientIpLocation.getCountry_code())){
            String criteria = feeRule.getSqlRestrictions();
            long days = ChronoUnit.DAYS.between(feeRequestDto.getCommercialrelation().getFirstmission(),
                    feeRequestDto.getCommercialrelation().getLastMission());
            SearchRuleCriteria searchRuleCriteria = SearchRuleCriteria.builder()
                    .commercialrelation_duration(days)
                    .mission_duration(getNbDaysFromDuration(feeRequestDto.getMission().getLength()))
                    .build();
            if(checkRuleOnCriteria(criteria, searchRuleCriteria, feeRule.getId())){
                FeeDto feeDto = new FeeDto();
                feeDto.setFees((int) feeRule.getRate());
                feeDto.setReason(feeRule.getName());
                return feeDto;
            }
        }
        FeeDto feeDto = new FeeDto();
        FeeRule feeRule = feeRuleService.findDefaultFeeRule();
        feeDto.setFees((int) feeRule.getRate());
        return feeDto;
    }

    private static long getNbDaysFromDuration(String durationRestriction) {
        Duration duration  = DurationConverter.parse(durationRestriction);
        return duration.toDays();
    }
}
