CREATE TABLE diners (
                        id BIGSERIAL PRIMARY KEY,
                        name VARCHAR(255) NOT NULL,
                        dietary_restrictions JSON
);

CREATE TABLE restaurants (
                             id BIGSERIAL PRIMARY KEY,
                             name VARCHAR(255) NOT NULL,
                             endorsements JSON
);

CREATE TABLE tables (
                        id BIGSERIAL PRIMARY KEY,
                        restaurant_id BIGINT REFERENCES restaurants(id),
                        capacity INT NOT NULL
);

CREATE TABLE reservations (
                              id BIGSERIAL PRIMARY KEY,
                              table_id BIGINT REFERENCES tables(id),
                              diner_ids BIGINT[], -- Array of diner IDs
                              reservation_time TIMESTAMPTZ NOT NULL,
                              end_time TIMESTAMPTZ NOT NULL -- End time as reservation_time + 2 hours
);

CREATE OR REPLACE FUNCTION check_reservation_overlap()
    RETURNS TRIGGER AS $$
BEGIN
    IF EXISTS (
        SELECT 1
        FROM reservations r
        WHERE r.table_id = NEW.table_id
          AND r.id <> NEW.id -- Ensure not comparing with itself
          AND r.reservation_time < (NEW.reservation_time + INTERVAL '2 hours')
          AND (r.reservation_time + INTERVAL '2 hours') > NEW.reservation_time
    ) THEN
        RAISE EXCEPTION 'Reservation overlaps with an existing reservation.';
END IF;
RETURN NEW;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER reservation_overlap
    BEFORE INSERT OR UPDATE ON reservations
                         FOR EACH ROW
                         EXECUTE FUNCTION check_reservation_overlap();

INSERT INTO diners (name, dietary_restrictions)
VALUES
    ('Jack', '["Vegan"]'),
    ('Jill', '["Vegetarian"]'),
    ('Jane', '["Paleo"]');

INSERT INTO diners (name, dietary_restrictions)
VALUES
    ('Alice', '["Vegan"]'),
    ('Bob', '["Vegan"]'),
    ('Charlie', '["Vegan"]');


INSERT INTO restaurants (name, endorsements)
VALUES
    ('Restaurant A', '["Vegan"]'),
    ('Restaurant B', '["Vegetarian"]');

INSERT INTO tables (restaurant_id, capacity)
VALUES
    (1, 4),
    (2, 6);

INSERT INTO reservations (table_id, diner_ids, reservation_time, end_time)
VALUES
    (1, ARRAY[1, 2], '2024-08-01 19:00:00', '2024-08-01 21:00:00');

Select * From diners;

