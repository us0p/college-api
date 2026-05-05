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
    id int PK
    username varchar(20) UNIQUE NOT NULL
    password_hash varchar(60) NOT NULL
    email varchar(254) UNIQUE NOT NULL
    phone_number varchar(20) UNIQUE
    role_id int NOT NULL references roles(id)
    ra varchar(10) UNIQUE
}

Table posts {
    id int PK
    user_id int NOT NULL references users(id)
    title varchar(200) NOT NULL
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
    knowledge_base boolean NOT NULL default false
}

Table document_embedding {
    id int PK
    document_id int NOT NULL references documents(id)
    embedding vector(768) NOT NULL
}
