package dev.bdon.glasses;

import com.tngtech.archunit.core.domain.JavaClasses;
import com.tngtech.archunit.junit.AnalyzeClasses;
import com.tngtech.archunit.junit.ArchTest;
import com.tngtech.archunit.lang.syntax.ArchRuleDefinition;

@AnalyzeClasses(packages = "dev.bdon")
public class DependencyRulesTest {

    @ArchTest
    public void noClassesInPackageADependOnClassesInPackageB(JavaClasses importedClasses) {
        ArchRuleDefinition
            .noClasses().that().resideInAPackage("dev.bdon.glasses.path..")
            .should().dependOnClassesThat().resideInAPackage("dev.bdon.glasses.lens..")
            .andShould().dependOnClassesThat().resideInAPackage("dev.bdon.glasses.type..")
            .check(importedClasses);

        ArchRuleDefinition
            .noClasses().that().resideInAPackage("dev.bdon.glasses.type..")
            .should().dependOnClassesThat().resideInAPackage("dev.bdon.glasses.lens..")
            .andShould().dependOnClassesThat().resideInAPackage("dev.bdon.glasses.path..")
            .check(importedClasses);

        ArchRuleDefinition
            .noClasses().that().resideInAPackage("dev.bdon.glasses.util..")
            .should().dependOnClassesThat().resideInAPackage("dev.bdon.glasses.lens..")
            .andShould().dependOnClassesThat().resideInAPackage("dev.bdon.glasses.path..")
            .andShould().dependOnClassesThat().resideInAPackage("dev.bdon.glasses.type..")
            .check(importedClasses);
    }
}