Table permission_objects {
    id int PK
    name varchar(20) UNIQUE NOT NULL
}

Table roles {
    id int PK
    name varchar(20) UNIQUE NOT NULL
}

Table role_permissions {
    id int PK
    role_id int NOT NULL references roles(id)
    permission_id int NOT NULL references permission_objects(id)
}

Table users {
    username varchar(20) PK
    role_id int NOT NULL references roles(id)
    ra varchar(10) UNIQUE
}

Table posts {
    id int PK
    user_id int NOT NULL references users(id)
    markdown_content text NOT NULL
    created_at datetimetz NOT NULL
    updated_at datetimetz NOT NULL
    deleted_at datetimetz
}

Table documents {
    id int PK
    user_id int NOT NULL references users(id)
    file_name varchar(100) UNIQUE NOT NULL
    description text
    file_size int NOT NULL
    bucket_url varchar(255) UNIQUE NOT NULL 
}

Table document_embedding {
    id int PK
    document_id int NOT NULL references documents(id)
    embedding vector(768) NOT NULL
}
