ARG POSTGRES_PASSWORD
FROM postgres:16

COPY tables.sql /docker-entrypoint-initdb.d/

ENV POSTGRES_PASSWORD ${POSTGRES_PASSWORD}
ENV POSTGRES_DB drinkingfountain