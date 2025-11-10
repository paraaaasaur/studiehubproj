-- first, CREATE DATABASE [STUDIEHUB]; as SA
-- because db creation is beyond general ddl scope
USE [STUDIEHUB];

DROP TABLE IF EXISTS cart_item;
DROP TABLE IF EXISTS order_info;
DROP TABLE IF EXISTS Entryform;
DROP TABLE IF EXISTS EventInfo;
DROP TABLE IF EXISTS Question_Info;
DROP TABLE IF EXISTS chat_Reply;
DROP TABLE IF EXISTS chat_Info;
DROP TABLE IF EXISTS Rating;
DROP TABLE IF EXISTS ProductInfo;
DROP TABLE IF EXISTS user_info;

SET ANSI_NULLS ON;
SET QUOTED_IDENTIFIER ON;

-- 1. user_info
CREATE TABLE [user_info] (
    [u_id] VARCHAR(255),
    [mimeType] VARCHAR(255),
    [u_address] NVARCHAR(255),
    [u_birthday] DATE,
    [u_email] VARCHAR(255) NOT NULL,
    [u_firstname] NVARCHAR(255) NOT NULL,
    [u_gender] NVARCHAR(255),
    [u_img] VARBINARY(MAX),
    [u_lastname] NVARCHAR(255) NOT NULL,
    [u_psw] VARCHAR(255) NOT NULL,
    [u_tel] VARCHAR(255),
    -- constraints
    CONSTRAINT PK_user_info PRIMARY KEY (u_id)
);

-- 2-1. ProductInfo
CREATE TABLE [ProductInfo] (
    [p_ID] INT IDENTITY,
    [p_Class] NVARCHAR(255),
    [p_DESC] NVARCHAR(MAX),
    [p_Img] NVARCHAR(255),
    [p_Name] NVARCHAR(255) NOT NULL,
    [p_Price] INT,
    [p_Status] INT,
    [p_Video] NVARCHAR(255),
    [p_createDate] DATETIME2,
    [u_ID] VARCHAR(255),
    -- constraints
    CONSTRAINT PK_ProductInfo PRIMARY KEY (p_ID),
    CONSTRAINT FK_ProductInfo_u_ID_user_info FOREIGN KEY (u_ID) REFERENCES user_info(u_id)
);

-- 2-2. Rating
CREATE TABLE [Rating] (
    [r_ID] INT IDENTITY,
    [comment] NVARCHAR(MAX),
    [p_ID] INT,
    [ratedIndex] INT,
    [rating_count] INT,
    -- constraints
    CONSTRAINT PK_Rating PRIMARY KEY (r_ID),
    CONSTRAINT FK_Rating_p_ID_ProductInfo FOREIGN KEY (p_ID) REFERENCES ProductInfo(p_ID)
);

-- 3-1. chat_Info
CREATE TABLE [chat_Info] (
    [c_ID] INT IDENTITY,
    [c_Class] NVARCHAR(255),
    [c_Conts] NVARCHAR(255),
    [c_Date] VARCHAR(255),
    [c_Title] NVARCHAR(255),
    [U_ID] VARCHAR(255),
    -- constraints
    CONSTRAINT PK_chat_Info PRIMARY KEY (c_ID),
    CONSTRAINT FK_chat_Info_U_ID_user_info FOREIGN KEY (U_ID) REFERENCES user_info(u_id)
);

-- 3-2. chat_Reply
CREATE TABLE [chat_Reply] (
    [c_ID] INT IDENTITY,
    [c_Conts] NVARCHAR(255),
    [c_Date] VARCHAR(255),
    [C_IDr] INT,
    [U_ID] VARCHAR(255),
    -- constraints
    CONSTRAINT PK_chat_Reply PRIMARY KEY (c_ID),
    CONSTRAINT FK_chat_Reply_C_IDr_chat_Info FOREIGN KEY (C_IDr) REFERENCES chat_Info(c_ID),
    CONSTRAINT FK_chat_Reply_U_ID_user_info FOREIGN KEY (U_ID) REFERENCES user_info(u_id)
);

-- 4. Question_Info
CREATE TABLE [Question_Info] (
    [q_id] BIGINT IDENTITY,
    [createDate] DATETIME2,
    [mimeTypeAudio] VARCHAR(255),
    [mimeTypePic] VARCHAR(255),
    [q_answer] NVARCHAR(255) NOT NULL,
    [q_audio] VARBINARY(MAX),
    [q_class] NVARCHAR(255),
    [q_picture] VARBINARY(MAX),
    [q_question] NVARCHAR(255) NOT NULL,
    [q_selectionA] NVARCHAR(255) NOT NULL,
    [q_selectionB] NVARCHAR(255) NOT NULL,
    [q_selectionC] NVARCHAR(255) NOT NULL,
    [q_selectionD] NVARCHAR(255) NOT NULL,
    [q_selectionE] NVARCHAR(255),
    [q_type] NVARCHAR(255),
    [verification] VARCHAR(255),
    -- constraints
    CONSTRAINT PK_Question_Info PRIMARY KEY (q_id)
);

-- 5-1. EventInfo
CREATE TABLE [EventInfo] (
    [a_aid] BIGINT IDENTITY,
    [a_address] NVARCHAR(255),
    [a_endTime] DATETIME2,
    [a_name] NVARCHAR(255),
    [a_picturepath] NVARCHAR(1000),
    [a_registration_endrttime] DATETIME2,
    [a_registration_starttime] DATETIME2,
    [a_startTime] DATETIME2,
    [a_type] NVARCHAR(255),
    [a_uid] VARCHAR(255),
    [applicants] INT NOT NULL,
    [comment] NVARCHAR(MAX),
    [creationTime] DATETIME2,
    [expired] NVARCHAR(255),
    [havesignedup] INT NOT NULL,
    [uidname] NVARCHAR(255),
    [verification] VARCHAR(255),
    -- constraints
    CONSTRAINT PK_EventInfo PRIMARY KEY (a_aid)
);

-- 5-2. Entryform
CREATE TABLE [Entryform] (
    [id] BIGINT IDENTITY,
    [e_email] VARCHAR(255),
    [e_firstname] NVARCHAR(255),
    [e_id] VARCHAR(255),
    [e_lastname] NVARCHAR(255),
    [e_tel] VARCHAR(255),
    [eventInfo_a_aid] BIGINT,
    -- constraints
    CONSTRAINT PK_Entryform PRIMARY KEY (id),
    CONSTRAINT FK_Entryform_eventInfo_a_aid_EventInfo FOREIGN KEY (eventInfo_a_aid) REFERENCES EventInfo(a_aid)
);

-- 6-1. order_info
CREATE TABLE [order_info] (
    [identity_seed] INT IDENTITY,
    [ECPAY_O_ID] VARCHAR(255),
    [ecpay_trade_no] VARCHAR(255),
    [o_amt] INT,
    [o_date] DATETIME DEFAULT CURRENT_TIMESTAMP,
    [o_id] INT,
    [o_status] NVARCHAR(100) DEFAULT '完成',
    [P_ID] INT,
    [p_name] NVARCHAR(255),
    [p_price] INT,
    [u_email] VARCHAR(255),
    [u_firstname] NVARCHAR(255),
    [U_ID] VARCHAR(255),
    [u_lastname] NVARCHAR(255),
    -- constraints
    CONSTRAINT PK_order_info PRIMARY KEY (identity_seed),
    CONSTRAINT FK_order_info_P_ID_ProductInfo FOREIGN KEY (P_ID) REFERENCES ProductInfo(p_ID),
    CONSTRAINT FK_order_info_U_ID_user_info FOREIGN KEY (U_ID) REFERENCES user_info(u_id)
);

-- 6.2 cart_item
CREATE TABLE [cart_item] (
    [cart_id] INT IDENTITY,
    [cart_date] DATETIME DEFAULT CURRENT_TIMESTAMP,
    [P_ID] INT,
    [p_name] NVARCHAR(255),
    [p_price] INT,
    [u_firstname] NVARCHAR(255),
    [U_ID] VARCHAR(255),
    [u_lastname] NVARCHAR(255),
    -- constraints
    CONSTRAINT PK_cart_item PRIMARY KEY (cart_id),
    CONSTRAINT FK_cart_item_P_ID_ProductInfo FOREIGN KEY (P_ID) REFERENCES ProductInfo(p_ID),
    CONSTRAINT FK_cart_item_U_ID_user_info FOREIGN KEY (U_ID) REFERENCES user_info(u_id)
);