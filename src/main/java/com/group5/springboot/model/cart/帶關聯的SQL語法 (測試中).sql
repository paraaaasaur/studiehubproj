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
INSERT [dbo].[User_Info] ([u_id], [u_psw], [u_birthday], [u_lastname], [u_firstname], [u_img], [u_email], [u_tel], [u_gender], [u_address]) VALUES (N'josh', N'josh', CAST(N'1997-08-18' AS Date), N'Sun', N'Joshua', NULL, N'josh@email', N'0987654321', N'男', N'TWTYC')
GO
INSERT [dbo].[User_Info] ([u_id], [u_psw], [u_birthday], [u_lastname], [u_firstname], [u_img], [u_email], [u_tel], [u_gender], [u_address]) VALUES (N'ken', N'kenken', CAST(N'2021-12-31' AS Date), N'Huang', N'Ken', NULL, N'freakinpink@gmail', N'12345', N'男', N'TW')
GO
INSERT [dbo].[User_Info] ([u_id], [u_psw], [u_birthday], [u_lastname], [u_firstname], [u_img], [u_email], [u_tel], [u_gender], [u_address]) VALUES (N'nick', N'nick', CAST(N'2021-12-31' AS Date), N'Chung', N'Meng Hua', NULL, N'nick@gmail.com', N'45678', N'男', N'TW')
GO
INSERT [dbo].[User_Info] ([u_id], [u_psw], [u_birthday], [u_lastname], [u_firstname], [u_img], [u_email], [u_tel], [u_gender], [u_address]) VALUES (N'tajen', N'tajen', CAST(N'2021-12-31' AS Date), N'Wang', N'Ta Jen', NULL, N'tajen@gmail.com', N'23456', N'男', N'TW')
GO
INSERT [dbo].[User_Info] ([u_id], [u_psw], [u_birthday], [u_lastname], [u_firstname], [u_img], [u_email], [u_tel], [u_gender], [u_address]) VALUES (N'test0608', N'tt', CAST(N'1900-01-01' AS Date), N'test', N'testfirst', NULL, N'test@email', N'0123456789', N'女', N'桃園市')
GO
INSERT [dbo].[User_Info] ([u_id], [u_psw], [u_birthday], [u_lastname], [u_firstname], [u_img], [u_email], [u_tel], [u_gender], [u_address]) VALUES (N'test0609', N'test0609', CAST(N'2021-06-09' AS Date), N'test0609', N'test0609', NULL, N'0609@email', N'06099999', N'女', N'TW')
GO
INSERT [dbo].[User_Info] ([u_id], [u_psw], [u_birthday], [u_lastname], [u_firstname], [u_img], [u_email], [u_tel], [u_gender], [u_address]) VALUES (N'test0610', N'test0610', CAST(N'2021-06-10' AS Date), N'測試', N'測試名字', NULL, N'0610@email', N'987654321', N'女', N'台北市')
GO
INSERT [dbo].[User_Info] ([u_id], [u_psw], [u_birthday], [u_lastname], [u_firstname], [u_img], [u_email], [u_tel], [u_gender], [u_address]) VALUES (N'test06101', N'test06101', CAST(N'2021-06-10' AS Date), N'0610姓', N'0610名', NULL, N'0610@email', N'0987654321', N'女', N'新北市')
GO
INSERT [dbo].[User_Info] ([u_id], [u_psw], [u_birthday], [u_lastname], [u_firstname], [u_img], [u_email], [u_tel], [u_gender], [u_address]) VALUES (N'yen', N'yen', CAST(N'2021-12-31' AS Date), N'Yen', N'Jia Cheng', NULL, N'yen@gmail.com', N'56789', N'男', N'TW')
GO
INSERT [dbo].[User_Info] ([u_id], [u_psw], [u_birthday], [u_lastname], [u_firstname], [u_img], [u_email], [u_tel], [u_gender], [u_address]) VALUES (N'yuz', N'yuz', CAST(N'2021-12-31' AS Date), N'Tu', N'Yu Zhe', NULL, N'yuz@gmail.com', N'34567', N'男', N'TW')
GO

-- product part
INSERT INTO ProductInfo VALUES ('EN_Speaking', 'EN', 5006, 'lul', null, '1999-12-12', 100, 100)
INSERT ProductInfo VALUES ('RU_READING', 'RU', 6000, 'NICE N FUN', 'ken', '2000-01-02', 100, 100)
-- order part
/*
INSERT [dbo].[Order_Info] VALUES (1, N'EN_Speaking', 300, N'elf001', N'fl', N'b', N'w@w.w', N'cancelled', CAST(N'1901-12-01T00:00:00' AS SmallDateTime), 300)
INSERT [dbo].[Order_Info] VALUES (6, N'JP_Speaking', 500, N'fox002', N'fu', N'b', N'x@x.x', N'done', CAST(N'1902-12-02T00:00:00' AS SmallDateTime), 500)
INSERT [dbo].[Order_Info] VALUES (49, N'EN_Reading', 900, N'sheep004', N'wa', N'b', N'y@y.y', N'cancelled', CAST(N'1903-12-03T00:00:00' AS SmallDateTime), 900)
INSERT [dbo].[Order_Info] VALUES (1753, N'DE_Speaking', 799, N'alien909', N'erf', N'id', N'z@z.z', N'done', CAST(N'1904-06-03T00:00:00' AS SmallDateTime), 799)
INSERT [dbo].[Order_Info] VALUES (100201, N'ES_Writing', 199, N'893doragon', N'cc', N'msg', N'a@a.a', N'done', CAST(N'1905-01-01T00:00:00' AS SmallDateTime), 762)
INSERT [dbo].[Order_Info] VALUES (057771, N'SV_Translation', 563, N'bark563', N'knr', N'ingm', N'b@b.b', N'done', CAST(N'1906-11-22T22:05:00' AS SmallDateTime), 762)
GO
*/
INSERT Order_Info VALUES (1, N'EN_Speaking', 5006, 'tajen', N'fl', N'b', N'w@w.w', DEFAULT, DEFAULT, 300)
INSERT Order_Info VALUES (1, 'RU_READING', 6000, 'tajen', 'jen', 'ta', '1@2.3', DEFAULT, DEFAULT, 999)
GO