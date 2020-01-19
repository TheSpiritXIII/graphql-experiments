package com.example.springboot.comment;

import com.example.springboot.graphql.mixin.GraphQLMixin;

import graphql.annotations.annotationTypes.GraphQLField;
import graphql.annotations.annotationTypes.GraphQLName;

@GraphQLMixin(Comment.class)
public interface CommentMixin {
	@GraphQLField
	@GraphQLName("content")
	public String getContent();
}
