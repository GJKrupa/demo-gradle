package uk.test.xyx.plugins.docker_integration_test

import org.gradle.api.tasks.testing.Test

import java.util.concurrent.TimeUnit

class DockerIntegrationTestTask extends Test {

    def imageGroup = project.group
    def imageName = project.name
    def imageVersion = project.version
    def waitForText
    def readyTimeout = 10000
    def portMap = [8080, 18080]

    def startDocker() {
        def pb = new ProcessBuilder("docker", "run", "-d", "-p${portMap[1]}:${portMap[0]}", "${imageGroup}/${imageName}:${imageVersion}")
        pb.redirectErrorStream(true)
        def process = pb.start()
        def containerId

        InputStream stdout = process.getInputStream()
        BufferedReader reader = new BufferedReader(new InputStreamReader(stdout))

        def line
        while ((line = reader.readLine()) != null) {
            containerId = line.trim()
            println line
        }

        int result = process.waitFor()

        if (result != 0) {
            throw new IllegalStateException("Docker run did not return 0")
        }

        return containerId
    }

    def waitForDockerStart(containerId) {
        def pb = new ProcessBuilder("docker", "logs", "-f", containerId)
        pb.redirectErrorStream(true)
        def process = pb.start()

        InputStream stdout = process.getInputStream()
        BufferedReader reader = new BufferedReader(new InputStreamReader(stdout))

        def ready = false;

        Thread.start {
            def line
            while ( ( line = reader.readLine ( ) ) != null ) {
                println line
                if (line.contains(waitForText)) {
                    println "Container is ready"
                    ready = true
                    break
                }
            }
            process.destroyForcibly()
        }

        process.waitFor(readyTimeout, TimeUnit.MILLISECONDS)
        if (!ready) {
            process.destroyForcibly()
            throw new IllegalStateException("Container was not ready in ${readyTimeout}ms")
        }
    }

    def killDocker(containerId) {
        def pb = new ProcessBuilder("docker", "rm", "-f", containerId)
        pb.redirectErrorStream(true)
        def process = pb.start()
        if (process.waitFor() != 0) {
            throw new IllegalStateException("Failed to kill docker container $containerId")
        }
    }

    void executeTests() {
        def containerId = startDocker()
        try {
            waitForDockerStart(containerId)
            super.executeTests()
        } finally {
            killDocker(containerId)
        }
    }
}
