package com.example.springboot.address;

import com.example.springboot.util.Database;
import com.github.javafaker.Faker;

final public class AddressDatabase extends Database<Address> {
	@Override
	protected Address generate(Faker faker) {
		Address address = new Address();
		address.streetNumber = faker.address().streetAddress();
		address.city = faker.address().city();
		address.country = faker.address().country();
		return address;
	}

	@Override
	protected long getId(Address data) {
		return data.id;
	}

	@Override
	protected void setId(Address data, long id) {
		data.id = id;
	}
}
