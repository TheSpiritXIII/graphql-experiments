package com.example.springboot.comment;

import com.example.springboot.graphql.mixin.GraphQLMixin;

import graphql.annotations.annotationTypes.GraphQLField;

@GraphQLMixin(Comment.class)
public interface CommentMixin {
	@GraphQLField
	public String getContent();
}
