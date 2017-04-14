package edu.hendrix.csci250.csci250proj4;

import java.time.LocalDate;

import javafx.scene.paint.Color;

public class User {
	private String name;
	private LocalDate birthday;
	private Color favoriteColor;

	public User(String name, LocalDate birthday, Color favoriteColor) {
		this.setName(name);
		this.setBirthday(birthday);
		this.setFavoriteColor(favoriteColor);
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public LocalDate getBirthday() {
		return birthday;
	}

	public void setBirthday(LocalDate birthday) {
		this.birthday = birthday;
	}

	public Color getFavoriteColor() {
		return favoriteColor;
	}

	public void setFavoriteColor(Color favoriteColor) {
		this.favoriteColor = favoriteColor;
	}

}
