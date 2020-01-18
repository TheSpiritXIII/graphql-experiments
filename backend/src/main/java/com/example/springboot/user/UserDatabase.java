package com.example.springboot.user;

import com.example.springboot.util.Database;
import com.github.javafaker.Faker;

public final class UserDatabase extends Database<User> {
	@Override
	protected User generate(Faker faker) {
		User user = new User();
		user.name = faker.name().fullName();
		user.email = user.name.replaceAll("\\s", ".") + "@example.com";
		user.addressId = (long) this.getAll().size();
		return user;
	}

	@Override
	protected long getId(User data) {
		return data.id;
	}

	@Override
	protected void setId(User data, long id) {
		data.id = id;
	}
}
