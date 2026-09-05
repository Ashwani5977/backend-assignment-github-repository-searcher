package com.ashwani.githubrepositorysearcher.dto.external;

public class GithubOwner {

    private String login;

    public GithubOwner() {

    }

    public GithubOwner(String login) {
        this.login = login;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }
}
