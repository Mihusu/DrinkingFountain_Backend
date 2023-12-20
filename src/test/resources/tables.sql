--User the drinkingfountain database
CREATE DATABASE drinkingfountain;

-- Create a new PostgreSQL table called "users"
CREATE TABLE users (
    user_id SERIAL PRIMARY KEY, -- Auto-incrementing unique identifier for each user
    name VARCHAR(100) UNIQUE NOT NULL, -- Name of the user, cannot be NULL
    password VARCHAR(100) NOT NULL, -- Password of the user, cannot be NULL
    role VARCHAR(100) NOT NULL,
    created_at TIMESTAMP WITH TIME ZONE NOT NULL -- Timestamp indicating when the user was created, defaults to current timestamp
);


CREATE TABLE drinking_fountains (
    drinking_fountain_id SERIAL PRIMARY KEY, -- Auto-incrementing unique identifier for each drinking fountain
    longitude DOUBLE PRECISION NOT NULL, -- Longitude coordinate of a drinking fountain, cannot be NULL
    latitude DOUBLE PRECISION NOT NULL, -- Latitude coordinate of a drinking fountain, cannot be NULL
    fountain_type VARCHAR(100) NOT NULL, -- Types of fountain that exists, cannot be NULL
    approved BOOLEAN NOT NULL, -- Checks if the drinking fountain is approved to be place and visible on the map, cannot be NULL
    score DOUBLE PRECISION, -- The average score for a drinking fountain based on the users star rate
    created_at TIMESTAMP WITH TIME ZONE NOT NULL -- Timestamp indicating when the drinking_fountain was created, defaults to current timestamp
);


CREATE TABLE reviews (
    review_id SERIAL PRIMARY KEY, -- Auto-incrementing unique identifier for each review
    text VARCHAR(1000) NOT NULL, -- cannot be NULL
    stars int NOT NULL, -- cannot be NULL
    fountain_type VARCHAR(1000) NOT NULL, -- Types of fountain that exists, cannot be NULL
    user_id SERIAL REFERENCES users(user_id), --  Unique identifier that comes from the user table as a foreign key
    drinking_fountain_id SERIAL REFERENCES drinking_fountains(drinking_fountain_id), -- Unique identifier that comes from the drinking_fountain table as a foreign key
    created_at TIMESTAMP WITH TIME ZONE NOT NULL -- Timestamp indicating when the review was created, defaults to current timestamp
);

CREATE TABLE fountain_images (
    fountain_images_id SERIAL PRIMARY KEY, -- Auto-incrementing unique identifier for each fountain image
    image BYTEA NOT NULL, -- Storing the images of a drinking fountain, cannot be NULL
    drinking_fountain_id SERIAL REFERENCES drinking_fountains(drinking_fountain_id), -- Unique identifier that comes from the drinking_fountain table as a foreign key
    created_at TIMESTAMP WITH TIME ZONE NOT NULL -- Timestamp indicating when the fountain_images was created, defaults to current timestamp
);