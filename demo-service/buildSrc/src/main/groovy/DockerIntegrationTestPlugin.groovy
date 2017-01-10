import org.gradle.api.Plugin
import org.gradle.api.Project

class DockerIntegrationTestPlugin implements Plugin<Project> {
    void apply(Project project) {
        project.task('dockerIntegrationTest', type: DockerIntegrationTestTask) {
        }
    }
}

