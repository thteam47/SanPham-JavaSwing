create database QLSanpham
go

use QLSanpham
go
CREATE TABLE [dbo].[TAIKHOAN](
	[tai_khoan] [varchar](15) primary key,
	[mat_khau] [varchar](15) NOT NULL,
	cauhoi nvarchar(50) null,
	traloi nvarchar(50) null
)
go
CREATE TABLE [dbo].[SanPham](
	maSp bigint IDENTITY(1,1) primary key,
	[tenSp] [nvarchar](50) NOT NULL,
	[soLuong] int NULL,
	[donGia] int NULL,
)
go

insert TAIKHOAN
values('admin','123',null,null)
go


INSERT INTO SanPham (tenSp, soLuong, donGia) VALUES
(N'Bút mực',10, 2000),
(N'Bút chì',20, 5000),
(N'Bút bi',10, 10000);
