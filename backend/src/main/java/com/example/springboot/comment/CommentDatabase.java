package com.example.springboot.comment;

import java.time.ZoneId;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import com.example.springboot.util.Database;
import com.github.javafaker.Faker;

public final class CommentDatabase extends Database<Comment> {
	public CommentDatabase(int amount) {
		super(amount);
	}

	@Override
	protected Comment generate(Faker faker) {
		Comment comment = new Comment();
		comment.setContent(faker.elderScrolls().quote());
		Date createdDate = faker.date().past(10, TimeUnit.DAYS);
		comment.setCreatedTime(createdDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime());
		comment.setUserId(faker.random().nextInt(0, 5));
		return comment;
	}

	@Override
	protected long getId(Comment data) {
		return data.getId();
	}

	@Override
	protected void setId(Comment data, long id) {
		data.setId(id);
	}
}
