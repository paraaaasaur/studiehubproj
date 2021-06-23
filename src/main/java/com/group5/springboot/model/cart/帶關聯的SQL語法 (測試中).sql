USE [StudieHub]
GO
DROP TABLE IF EXISTS Order_Info, ProductInfo, User_Info
----- (1) User Table -----
CREATE TABLE [dbo].[User_Info](
	[u_id] [nvarchar](50) NOT NULL,
	[u_psw] [nvarchar](20) NOT NULL,
	[u_birthday] [date] NULL,
	[u_lastname] [nvarchar](20) NOT NULL,
	[u_firstname] [nvarchar](20) NOT NULL,
	[u_img] [varbinary](max) NULL,
	[u_email] [nvarchar](max) NOT NULL,
	[u_tel] [nvarchar](10) NULL,
	[u_gender] [nvarchar](10) NULL,
	[u_address] [nvarchar](max) NULL,
 CONSTRAINT [PK_User_Info] PRIMARY KEY CLUSTERED 
(
	[u_id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON, OPTIMIZE_FOR_SEQUENTIAL_KEY = OFF) ON [PRIMARY]
) ON [PRIMARY] TEXTIMAGE_ON [PRIMARY]
GO

----- (2) Product Table -----
-- 拿掉u_ID的not null & 加上 fk on update cascade
-- u_ID 主從的資料類型不同 (50,20)
CREATE TABLE [dbo].[ProductInfo](
	[p_ID] [int] IDENTITY(1,1) NOT NULL,
	[p_Name] [nvarchar](50) NOT NULL,
	[p_Class] [nchar](10) NOT NULL,
	[p_price] [int] NULL ,
	[p_DESC] [nvarchar](max) NOT NULL,
	[u_ID] [nvarchar](50) FOREIGN KEY REFERENCES User_Info(u_id),
	[p_createDate] [nvarchar](50) NOT NULL,
	[p_Img] [varbinary](max) NOT NULL,
	[p_Video] [varbinary](max) NOT NULL,
 CONSTRAINT [PK_ProductInfo] PRIMARY KEY CLUSTERED 
(
	[p_ID] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON, OPTIMIZE_FOR_SEQUENTIAL_KEY = OFF) ON [PRIMARY]
) ON [PRIMARY] TEXTIMAGE_ON [PRIMARY]
GO



----- (3) Order Table -----
-- pid 和 uid 加上fk on cascade update
-- o_date 加上 DAFAULT getdate()
-- o_status 加上 DEFAULT 'DONE'
-- u_id 應該有改成 50
CREATE TABLE [dbo].[Order_Info](
	[o_id] INT CONSTRAINT PK_o_ID PRIMARY KEY IDENTITY(1,1),
	[p_id] INT CONSTRAINT pidFK FOREIGN KEY REFERENCES ProductInfo(p_id) ON UPDATE CASCADE,
	[p_name] [nvarchar](50) ,
	[p_price] [int] ,
	[u_id] [nvarchar](50) CONSTRAINT uidFK FOREIGN KEY REFERENCES User_Info(u_id) ON UPDATE CASCADE,
	[u_firstName] [nvarchar](20) ,
	[u_lastName] [nvarchar](20) ,
	[u_email] [nvarchar](max) ,
	[o_status] [nvarchar](max) NOT NULL DEFAULT 'DONE',
	[o_date] [smalldatetime] NOT NULL DEFAULT getdate(),
	[o_amt] [int] 
)
GO
/* -------------------------------------------------------------------------------------------------------------------------------------------------- */

----- INSERT PART -----

DELETE FROM Order_Info
DELETE FROM ProductInfo
DELETE FROM User_Info
GO
-- user part
INSERT user_info VALUES ('fbk001', 'image/jpeg', 'galaxy', GETDATE(), 'c@a.t', 'Fubuki', 'female', 0x, 'Shirakami', 'kitsunejai', '0415')
GO

-- product part
INSERT ProductInfo VALUES ('RU', 'halashu', 0x, 'RU_Reading', 7000, null, '2000-01-02', 'fbk001')
INSERT ProductInfo VALUES ('EN', 'awesome', 0x, 'EN_Speaking', 6000, null, '1999-12-12', 'fbk001')
GO
-- order part
INSERT Order_Info VALUES (13000, DEFAULT, DEFAULT, 1, 'RU_Reading', 7000, 'c@a.t', 'Fubuki', 'fbk001', 'Shirakami')
GO
