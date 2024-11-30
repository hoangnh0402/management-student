use master
go

create database QLBanHang4_11
go
use QLBanHang4_11
go

DROP TABLE IF EXISTS dbo.HangSX
create table HangSX (
	MaHangSX nchar(10) primary key not null,
	TenHang nvarchar(20),
	DiaChi nvarchar(30),
	SoDienThoai nvarchar(20),
	Email nvarchar(30)
)

DROP TABLE IF EXISTS dbo.SanPham
create table SanPham (
	MaSP nchar(10) primary key not null,
	MaHangSX nchar(10),
	TenSP nchar (20),
	SoLuong int,
	MauSac nvarchar(20),
	GiaBan Money,
	DonViTinh nchar(10),
	MoTa nvarchar(max)
	constraint fk_sp_hangsx Foreign Key(MaHangSX)
	references HangSX(MaHangSX)
)

DROP TABLE IF EXISTS dbo.NhanVien
create table NhanVien (
	MaNV nchar(10) primary key not null,
	TenNV nvarchar(20),
	GioiTinh nchar(10),
	DiaChi nvarchar(30),
	SoDT nvarchar(20),
	Email nvarchar(30),
	TenPhong nvarchar(30)
)

DROP TABLE IF EXISTS dbo.PNhap
create table PNhap (
	SoHDN nchar(10) primary key not null,
	NgayNhap date,
	MaNV nchar(10),
	constraint fk_pnhap_nv Foreign Key(MaNV) references NhanVien(MaNV)
)

DROP TABLE IF EXISTS dbo.Nhap
create table Nhap (
	SoHDN nchar(10) not null,
	MaSP nchar(10) not null,
	SoLuongN int,
	DonGiaN money,
	constraint pk_nhap Primary Key(SoHDN, MaSP),
	constraint fk_nhap_pnhap Foreign Key(SoHDN) references PNhap(SoHDN),
	constraint fk_nhap_sp Foreign Key(MaSP) references SanPham(MaSP)
)

DROP TABLE IF EXISTS dbo.PXuat
create table PXuat(
	SoHDX nchar(10) primary key not null,
	NgayXuat date,
	MaNV nchar(10),
	constraint fk_pxuat_nv Foreign Key(MaNV) references NhanVien(MaNV)
)

DROP TABLE IF EXISTS dbo.Xuat
create table Xuat (
	SoHDX nchar(10) not null,
	MaSP nchar(10) not null,
	SoLuongX int,
	constraint pk_xuat Primary Key(SoHDX, MaSP),
	constraint fk_xuat_pxuat Foreign Key(SoHDX) references PXuat(SoHDX),
	constraint fk_xuat_sp Foreign Key(MaSP) references SanPham(MaSP)
)


insert into HangSX values
('H01', 'Samsung', 'Korea', '011-08271717', 'ss@gmail.com.kr'),
('H02', 'OPPO', 'China', '081-08626262', 'oppo@gmail.com.cn'),
('H03', 'Vinfone', N'Việt Nam', '084-098262626', 'vf@gmail.com.vn')

insert into NhanVien values
('NV01', N'Nguyễn Thị Thu', N'Nữ', N'Hà Nội', '0982626521', 'thu@gmail.com', N'Kê toán'),
('NV02', N'Lê Văn Nam', N'Nam', N'Bắc Ninh', '0972525252', 'nam@gmail.com', N'Vật tư'),
('NV03', N'Trần Hoà Bình', N'Nam', N'Hà Nội', '0328388388', 'hb@gmail.com', N'Kế toán')

insert into SanPham values
('SP1', 'H02', 'F1 Plus', 100, N'Xám', 7000000, N'Chi?c', N'Hàng c?n cao c?p'),
('SP2', 'H01', 'Galaxy Note 11', 50, N'Đ?', 19000000, N'Chi?c', N'Hàng cao c?p'),
('SP3', 'H02', 'F3 Lite', 200, N'Nâu', 3000000, N'Chi?c', N'Hàng ph? thông'),
('SP4', 'H03', 'Vjoy3', 200, N'Xám', 1500000, N'Chi?c', N'Hàng ph? thông'),
('SP5', 'H01', 'Galaxy V21', 500, N'Nâu', 8000000, N'Chi?c', N'Hàng c?n cao c?p')


insert into PNhap values
('N01', '2019-02-05', 'NV01'),
('N02', '2020-04-07', 'NV02'),
('N03', '2020-05-17', 'NV02'),
('N04', '2020-03-22', 'NV03'),
('N05', '2020-07-01', 'NV01')

insert into Nhap values
('N01', 'SP2', 10, 17000000),
('N02', 'SP1', 30, 6000000),
('N03', 'SP4', 20, 1200000),
('N04', 'SP1', 10, 6200000),
('N05', 'SP5', 20, 7000000)

insert into PXuat values
('X01', '2019-02-05', 'NV01'),
('X02', '2020-04-07', 'NV02'),
('X03', '2020-05-17', 'NV02'),
('X04', '2020-03-22', 'NV03'),
('X05', '2020-07-01', 'NV01')

insert into Xuat values
('X01', 'SP2', 5),
('X02', 'SP1', 3),
('X03', 'SP4', 1),
('X04', 'SP1', 2),
('X05', 'SP5', 1)

-- a. Đưa ra 10 mặt hàng có SoLuongN nhiều nhất trong năm 2019
select top 10 sp.MaSP, TenSP, sum(SoLuongN) as 'So luong nhap'
from SanPham sp
inner join Nhap n on sp.MaSP = n.MaSP
group by sp.MaSP, TenSP
order by sum(SoLuongN) desc

-- b. Đưa ra MaSP,TenSP của các sản phẩm do công ty ‘Samsung’ sản xuất do nhân viên có mã ‘NV01’ nhập
select sp.MaSP, TenSP
from SanPham sp
inner join HangSX hsx on sp.MaHangSX = hsx.MaHangSX
inner join Xuat x on sp.MaSP = x.MaSP
inner join PXuat px on x.SoHDX = px.SoHDX
where TenHang = 'Samsung' and MaNV = 'NV02'

-- c. Đưa ra SoHDN,MaSP,SoLuongN,ngayN của mặt hàng có MaSP là ‘SP02’, được nhân viên ‘NV02’ xuất.
select n.SoHDN, MaSP, SoLuongN, NgayNhap
from Nhap n
inner join PNhap pn on n.SoHDN = pn.SoHDN
where MaSP = 'SP2' and MaNV = 'NV02'

-- d. Đưa ra manv,TenNV đã xuất mặt hàng có mã ‘SP02’ ngày ‘03-02-2020’
select px.MaNV, TenNV 
from PXuat px
inner join Xuat x on px.SoHDX = x.SoHDX
inner join NhanVien nv on px.MaNV = nv.MaNV
where MaSP = 'SP2' and NgayXuat = '2020-02-03'





