-- Tabla principal de bootcamps
CREATE TABLE bootcamp (
    id UUID PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    description TEXT NOT NULL,
    launch_date DATE NOT NULL,
    duration VARCHAR(50) NOT NULL
);

