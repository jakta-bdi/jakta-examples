plugins {
    id("org.danilopianini.gradle-pre-commit-git-hooks") version "2.0.13"
    id("org.gradle.toolchains.foojay-resolver-convention") version "0.8.0"
}

gitHooks {
    preCommit {
        tasks("ktlintCheck")
    }
    commitMsg { conventionalCommits() }
    createHooks()
}

rootProject.name = "jakta-examples"
