plugins {
    id("org.danilopianini.gradle-pre-commit-git-hooks") version "2.0.15"
    id("org.gradle.toolchains.foojay-resolver-convention") version "0.9.0"
}

gitHooks {
    preCommit {
        tasks("ktlintCheck")
    }
    commitMsg { conventionalCommits() }
    createHooks()
}

rootProject.name = "jakta-examples"
