-- CreateTable
CREATE TABLE `categories` (
    `id` BIGINT NOT NULL AUTO_INCREMENT,
    `name` VARCHAR(6) NOT NULL,
    `type` VARCHAR(6) NULL,
    `code` VARCHAR(6) NULL,

    PRIMARY KEY (`id`)
) DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

-- CreateTable
CREATE TABLE `comments` (
    `id` BIGINT NOT NULL AUTO_INCREMENT,
    `content` TEXT NOT NULL,
    `post_id` BIGINT NOT NULL,
    `user_id` BIGINT NOT NULL,
    `created_at` TIMESTAMP(6) NOT NULL,
    `updated_at` TIMESTAMP(6) NOT NULL,
    `deleted_at` TIMESTAMP(6) NULL,

    INDEX `FK8omq0tc18jd43bu5tjh6jvraq`(`user_id`),
    INDEX `FKh4c7lvsc298whoyd4w9ta25cr`(`post_id`),
    PRIMARY KEY (`id`)
) DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

-- CreateTable
CREATE TABLE `email_verifications` (
    `id` BIGINT NOT NULL AUTO_INCREMENT,
    `email` VARCHAR(30) NOT NULL,
    `code` VARCHAR(10) NOT NULL,
    `complete` BOOLEAN NOT NULL,
    `created_at` TIMESTAMP(6) NOT NULL,
    `expired_at` TIMESTAMP(6) NOT NULL,

    PRIMARY KEY (`id`)
) DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

-- CreateTable
CREATE TABLE `follows` (
    `id` BIGINT NOT NULL AUTO_INCREMENT,
    `followed_user_id` BIGINT NOT NULL,
    `following_user_id` BIGINT NOT NULL,
    `created_at` DATETIME(6) NOT NULL,

    INDEX `FKfg3bo4whthco31ewoiaxpjhvi`(`followed_user_id`),
    INDEX `FKk1b1yn6h0224kelm91qetqxen`(`following_user_id`),
    PRIMARY KEY (`id`)
) DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

-- CreateTable
CREATE TABLE `post_likes` (
    `id` BIGINT NOT NULL AUTO_INCREMENT,
    `post_id` BIGINT NOT NULL,
    `user_id` BIGINT NOT NULL,

    INDEX `FK81m3sbgvf0vhy7lgufppa4o5`(`user_id`),
    INDEX `FKlcd6fjflxlit9llbxrmha8bfw`(`post_id`),
    PRIMARY KEY (`id`)
) DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

-- CreateTable
CREATE TABLE `posts` (
    `id` BIGINT NOT NULL AUTO_INCREMENT,
    `content` TEXT NULL,
    `thumbnail_url` TEXT NOT NULL,
    `image_url` TEXT NULL,
    `menu_tag` TEXT NULL,
    `like_count` INTEGER NOT NULL DEFAULT 0,
    `is_owner` BOOLEAN NOT NULL,
    `user_id` BIGINT NOT NULL,
    `place_id` BIGINT NOT NULL,
    `keyword_id` BIGINT NULL,
    `rating_id` BIGINT NOT NULL,
    `created_at` TIMESTAMP(6) NOT NULL,
    `updated_at` TIMESTAMP(6) NOT NULL,
    `deleted_at` TIMESTAMP(6) NULL,

    INDEX `FK5lidm6cqbc7u4xhqpxm898qme`(`user_id`),
    INDEX `FKonikjcopg9sp7kb852mjrxioj`(`rating_id`),
    INDEX `FKalrk2f4mefrfx07rjwcheb1jw`(`keyword_id`),
    INDEX `FKt38bd81etfla6c1ilk3s0wm45`(`place_id`),
    PRIMARY KEY (`id`)
) DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

-- CreateTable
CREATE TABLE `star_ratings` (
    `id` BIGINT NOT NULL AUTO_INCREMENT,
    `star` FLOAT NOT NULL,
    `post_id` BIGINT NOT NULL,

    INDEX `FKo6usan40orj7ig5w3t8udpqkx`(`post_id`),
    PRIMARY KEY (`id`)
) DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

-- CreateTable
CREATE TABLE `users` (
    `id` BIGINT NOT NULL AUTO_INCREMENT,
    `email` VARCHAR(30) NOT NULL,
    `password` TEXT NOT NULL,
    `name` VARCHAR(10) NOT NULL,
    `nickname` VARCHAR(10) NOT NULL,
    `phone_number` VARCHAR(15) NULL,
    `profile_image` TEXT NULL,
    `follower_count` INTEGER NOT NULL DEFAULT 0,
    `following_count` INTEGER NOT NULL DEFAULT 0,
    `role` ENUM('ADMIN', 'OWNER', 'USER') NOT NULL,
    `social_type` ENUM('GOOGLE', 'KAKAO', 'LOCAL', 'NAVER') NOT NULL,
    `status` ENUM('AWAIT', 'BAN', 'NORMAL') NOT NULL,
    `created_at` TIMESTAMP(6) NOT NULL,
    `updated_at` TIMESTAMP(6) NOT NULL,
    `deleted_at` TIMESTAMP(6) NULL,

    UNIQUE INDEX `UK2ty1xmrrgtn89xt7kyxx6ta7h`(`nickname`),
    PRIMARY KEY (`id`)
) DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

-- CreateTable
CREATE TABLE `places` (
    `id` BIGINT NOT NULL AUTO_INCREMENT,
    `name` VARCHAR(100) NOT NULL,
    `address` TEXT NOT NULL,
    `road_address` TEXT NULL,
    `telephone` VARCHAR(15) NULL,
    `x` FLOAT NOT NULL,
    `y` FLOAT NOT NULL,
    `category_id` BIGINT NOT NULL,
    `created_at` TIMESTAMP(6) NOT NULL,
    `updated_at` TIMESTAMP(6) NOT NULL,
    `deleted_at` TIMESTAMP(6) NULL,

    INDEX `FKca17w9kt9f18v0lr91ssmivx5`(`category_id`),
    PRIMARY KEY (`id`)
) DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

-- CreateTable
CREATE TABLE `post_keywords` (
    `id` BIGINT NOT NULL AUTO_INCREMENT,
    `category` VARCHAR(10) NOT NULL,
    `reaction` VARCHAR(15) NOT NULL,

    PRIMARY KEY (`id`)
) DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

-- CreateTable
CREATE TABLE `place_menus` (
    `id` BIGINT NOT NULL AUTO_INCREMENT,
    `menu` TEXT NOT NULL,
    `place_id` BIGINT NOT NULL,

    INDEX `FKca17w9kt9fv0lrefer91ssmivx5`(`place_id`),
    PRIMARY KEY (`id`)
) DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

-- AddForeignKey
ALTER TABLE `comments` ADD CONSTRAINT `FK8omq0tc18jd43bu5tjh6jvraq` FOREIGN KEY (`user_id`) REFERENCES `users`(`id`) ON DELETE NO ACTION ON UPDATE NO ACTION;

-- AddForeignKey
ALTER TABLE `comments` ADD CONSTRAINT `FKh4c7lvsc298whoyd4w9ta25cr` FOREIGN KEY (`post_id`) REFERENCES `posts`(`id`) ON DELETE NO ACTION ON UPDATE NO ACTION;

-- AddForeignKey
ALTER TABLE `follows` ADD CONSTRAINT `FKfg3bo4whthco31ewoiaxpjhvi` FOREIGN KEY (`followed_user_id`) REFERENCES `users`(`id`) ON DELETE CASCADE ON UPDATE NO ACTION;

-- AddForeignKey
ALTER TABLE `follows` ADD CONSTRAINT `FKk1b1yn6h0224kelm91qetqxen` FOREIGN KEY (`following_user_id`) REFERENCES `users`(`id`) ON DELETE CASCADE ON UPDATE NO ACTION;

-- AddForeignKey
ALTER TABLE `post_likes` ADD CONSTRAINT `FK81m3sbgvf0vhy7lgufppa4o5` FOREIGN KEY (`user_id`) REFERENCES `users`(`id`) ON DELETE NO ACTION ON UPDATE NO ACTION;

-- AddForeignKey
ALTER TABLE `post_likes` ADD CONSTRAINT `FKlcd6fjflxlit9llbxrmha8bfw` FOREIGN KEY (`post_id`) REFERENCES `posts`(`id`) ON DELETE CASCADE ON UPDATE NO ACTION;

-- AddForeignKey
ALTER TABLE `posts` ADD CONSTRAINT `FKonikjcopg9sp7kb852mjrxioj` FOREIGN KEY (`rating_id`) REFERENCES `star_ratings`(`id`) ON DELETE NO ACTION ON UPDATE NO ACTION;

-- AddForeignKey
ALTER TABLE `posts` ADD CONSTRAINT `FK5lidm6cqbc7u4xhqpxm898qme` FOREIGN KEY (`user_id`) REFERENCES `users`(`id`) ON DELETE NO ACTION ON UPDATE NO ACTION;

-- AddForeignKey
ALTER TABLE `posts` ADD CONSTRAINT `FKalrk2f4mefrfx07rjwcheb1jw` FOREIGN KEY (`keyword_id`) REFERENCES `post_keywords`(`id`) ON DELETE NO ACTION ON UPDATE NO ACTION;

-- AddForeignKey
ALTER TABLE `posts` ADD CONSTRAINT `FKt38bd81etfla6c1ilk3s0wm45` FOREIGN KEY (`place_id`) REFERENCES `places`(`id`) ON DELETE NO ACTION ON UPDATE NO ACTION;

-- AddForeignKey
ALTER TABLE `star_ratings` ADD CONSTRAINT `FKo6usan40orj7ig5w3t8udpqkx` FOREIGN KEY (`post_id`) REFERENCES `posts`(`id`) ON DELETE CASCADE ON UPDATE NO ACTION;

-- AddForeignKey
ALTER TABLE `places` ADD CONSTRAINT `FKca17w9kt9f18v0lr91ssmivx5` FOREIGN KEY (`category_id`) REFERENCES `categories`(`id`) ON DELETE NO ACTION ON UPDATE NO ACTION;

-- AddForeignKey
ALTER TABLE `place_menus` ADD CONSTRAINT `FKca17w9kt9fv0lrefer91ssmivx5` FOREIGN KEY (`place_id`) REFERENCES `places`(`id`) ON DELETE NO ACTION ON UPDATE NO ACTION;
