FROM postgres:10
ENV POSTGRES_DB ResistenceDataset
ENV POSTGRES_USER resistence
ENV POSTGRES_PASSWORD ZLn4W3LPanW2hZyF
COPY create.sql /docker-entrypoint-initdb.d/
COPY insert.sql /docker-entrypoint-initdb.d/
