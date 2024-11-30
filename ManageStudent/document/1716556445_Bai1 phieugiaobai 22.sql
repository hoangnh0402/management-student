use master
go
If(EXISTS(SELECT * FROM SysDatabases WHERE name = 'QLBH'))
DROP DATABASE QLBH
go
create database QLBH
on primary(
	name= 'QLBH_dat',
	filename='D:\QLBH.mdf',
	size=10MB,
	maxsize=100MB,
	filegrowth=10MB
)
log on(
	name='QLBH_log',
	filename='D:\QLBH.ldf',
	size=1MB,
	maxsize=5MB,
	filegrowth=20%
)
go
use QLBH
go 
create table CongTy(
	MaCT nchar(10) not null primary key,
	TenCT nvarchar(20) not null,
	TrangThai nchar(10),
	ThanhPho nvarchar(20)
)
create table SanPham(
	MaSP nchar(10) not null primary key,
	TenSP nvarchar(20),
	MauSac nchar(10) default N'Đỏ',
	Gia money,
	SoLuongCo int,
	constraint unique_SP unique(TenSP)
)
create table Cungung(
	MaCT nchar(10) not null,
	MaSP nchar(10) not null,
	SoLuongBan int,
	constraint PK_Cungung primary key(MaCT,MaSP),
	constraint chk_SLB check(SoLuongBan>0),
	constraint FK_CU_SP foreign key(MaSP)
		references SanPham(MaSP),
	constraint FK_CU_CT foreign key(MaCT)
		references CongTy(MaCT)
)
insert into CongTy Values('CT1','VNPT',N'tạm nghỉ',N'Bắc Giang')
insert into CongTy Values('CT2','FPT',N'hoạt động',N'Hải Dương')
insert into CongTy Values('CT3','VT',N'tạm nghỉ',N'Ninh Bình')
insert into SanPham Values('SP1','Cap','Cam',300,10)
insert into SanPham Values('SP2','Mouse',N'Hồng',500,12)
insert into SanPham Values('SP3','Screen',N'Đỏ',200,10)
insert into Cungung Values('CT1','SP1',3)
insert into Cungung Values('CT3','SP2',4)
insert into Cungung Values('CT2','SP3',2)
insert into Cungung Values('CT3','SP1',2)
insert into Cungung Values('CT1','SP3',7)
insert into Cungung Values('CT2','SP2',10)
SELECT * FROM CongTy
SELECT * FROM SanPham
SELECT * FROM Cungung