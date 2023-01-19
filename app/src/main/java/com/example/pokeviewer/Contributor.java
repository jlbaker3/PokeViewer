package com.example.pokeviewer;

import android.net.Uri;

import java.util.Objects;

public class Contributor {
    String name;
    Uri githubLink;

    public Contributor(String name, Uri githubLink) {
        this.name = name;
        this.githubLink = githubLink;
    }

    @Override
    public String toString() {
        return "Contributor{" +
                "name='" + name + '\'' +
                ", githubLink=" + githubLink +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Contributor that = (Contributor) o;
        return Objects.equals(name, that.name) && Objects.equals(githubLink, that.githubLink);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, githubLink);
    }
}
