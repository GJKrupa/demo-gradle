package uk.test.xyz.plugins.all_dependencies_specified

import org.gradle.api.InvalidUserDataException
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.artifacts.UnknownConfigurationException

class AllDependenciesSpecifiedPlugin implements Plugin<Project> {
    void apply(Project project) {
        if (!project.getTasksByName('compileJava', false).empty) {
            project.task('ensureAllDependenciesSpecified') << {
                println "Scanning dependencies for project ${project}"

                Set<String> declaredDeps = project.configurations.compile.incoming.dependencies.collect {
                    "${it.group}:${it.name}"
                }.unique()

                def unspecified = project.configurations.compile.incoming.resolutionResult.allDependencies.findAll {
                    !declaredDeps.contains("${it.selected.moduleVersion.group}:${it.selected.moduleVersion.name}")
                }.collect {
                    "compile '${it.selected}'"
                }.unique().sort()

                if (!unspecified.empty) {
                    throw new InvalidUserDataException(
                            "The following transitive dependencies have been detected in the compile classpath:\n" +
                                    unspecified.join("\n"))
                }
            }

            project.getTasksByName('compileJava', false).each {
                it.dependsOn 'ensureAllDependenciesSpecified'
            }
        }
    }
}

