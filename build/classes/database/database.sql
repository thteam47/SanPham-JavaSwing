create database QLNV
go

use QLNV
go
CREATE TABLE [dbo].[TAIKHOAN](
	[tai_khoan] [varchar](15) primary key,
	[mat_khau] [varchar](15) NOT NULL,
	cauhoi nvarchar(50) null,
	traloi nvarchar(50) null
)
go
CREATE TABLE [dbo].[NHANVIEN](
	maNV bigint IDENTITY(1,1) primary key,
	[hoTen] [nvarchar](50) NOT NULL,
	[ngaySinh] [date] NULL,
	[gioiTinh] [nvarchar](3) NULL,
	[diaChi] [nvarchar](200) NULL,
)
go

insert TAIKHOAN
values('admin','123',null,null)
go


INSERT INTO NHANVIEN (hoTen, diaChi, gioiTinh, ngaySinh) VALUES
(N'Phạm Thanh Nam',	N'Số 347, tổ 1, đường Phúc Diễn, Xuân Phương, Nam Từ Liêm',N'Nam','07/07/1980'),
(N'Chu Trung Toàn',	N'Số 2 đường Châu Văn Liêm, Phú Đô, Nam Từ Liêm',N'Nam',	'07/08/1980'),
(N'Triệu Văn Hiển',	N'Số 48, Tổ 2, Xuân Phương, Nam Từ Liêm',N'Nam',	'07/09/1980');
