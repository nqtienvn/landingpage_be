# BloomEcho Landing Page Backend

## Chạy local

- App: `http://localhost:8080`
- Swagger UI: `http://localhost:8080/swagger-ui.html`
- Admin mặc định được seed nếu chưa có user:
  - Username: `admin`
  - Password: `admin123`
- Có thể đổi bằng env: `ADMIN_USERNAME`, `ADMIN_PASSWORD`, `ADMIN_EMAIL`.
- Redis cache đang dùng Spring Cache abstraction. Mặc định `SPRING_CACHE_TYPE=simple`; bật Redis bằng `SPRING_CACHE_TYPE=redis`.

## PayOS (Thanh toán QR)

- SDK: `vn.payos:payos-java:2.0.1`
- Doc PayOS: https://payos.vn/docs/
- Quản lý API key tại: https://my.payos.vn

Env cần cấu hình:

| Biến | Mô tả |
|---|---|
| `PAYOS_CLIENT_ID` | Client ID từ PayOS |
| `PAYOS_API_KEY` | API Key từ PayOS |
| `PAYOS_CHECKSUM_KEY` | Checksum Key để verify webhook signature |
| `PAYOS_RETURN_URL` | URL frontend chuyển về sau thanh toán thành công |
| `PAYOS_CANCEL_URL` | URL frontend chuyển về khi khách huỷ thanh toán |

## Nhóm Entity chính

- `admin_users`: tài khoản admin, password BCrypt, role `ADMIN`.
- `media_assets`: ảnh đã upload Cloudinary, gồm `publicId`, `secureUrl`, `folder`, metadata.
- CMS:
  - `cms_hero_content`
  - `cms_manifesto_content`
  - `cms_visual_narrative_images`
  - `cms_craftsmanship_content`
  - `cms_craftsmanship_items`
  - `cms_footer_content`
  - `cms_social_links`
  - `cms_footer_policy_links`
- Catalog:
  - `collection_chapters`
  - `products`
  - `product_sizes`
  - `product_images`
- Orders:
  - `customer_orders` — thêm các cột PayOS: `payos_order_code`, `payos_payment_link_id`, `payos_checkout_url`, `payos_qr_code`
  - `order_items`
  - `bank_transfer_configs`

## Auth

`POST /api/auth/login`

```json
{
  "username": "admin",
  "password": "admin123"
}
```

Response trả `accessToken`. Các API `/api/admin/**` cần header:

```http
Authorization: Bearer <accessToken>
```

## Public API cho Landing Page

- `GET /api/public/landing-page`: trả toàn bộ cấu hình landing page gồm hero, manifesto, visual narrative, craftsmanship, footer, chapters, products.
- `GET /api/public/cms/hero`
- `GET /api/public/cms/manifesto`
- `GET /api/public/cms/visual-narrative`
- `GET /api/public/cms/craftsmanship`
- `GET /api/public/cms/footer`
- `GET /api/public/chapters`
- `GET /api/public/products`
- `GET /api/public/products/{slug}`
- `GET /api/public/payment/bank-transfer`
- `POST /api/public/orders`
- `GET /api/public/orders/{orderCode}`

PaymentMethod enum: `COD`, `BANK_TRANSFER`, `PAYOS`

Order response fields mới (PayOS):

| Field | Type | Mô tả |
|---|---|---|
| `payosOrderCode` | number | Mã đơn hàng trên hệ thống PayOS |
| `payosPaymentLinkId` | string | ID link thanh toán PayOS |
| `payosCheckoutUrl` | string | URL trang thanh toán PayOS (redirect khách đến đây) |
| `payosQrCode` | string | URL ảnh QR code VietQR |

Ví dụ tạo đơn hàng:

```json
{
  "customerName": "Nguyen Van A",
  "phone": "0900000000",
  "address": "Quan 1, TP.HCM",
  "note": "Giao giờ hành chính",
  "paymentMethod": "BANK_TRANSFER",
  "totalAmount": 2500000,
  "items": [
    {
      "productId": 1,
      "size": "M",
      "quantity": 1
    }
  ]
}
```

Nếu `paymentMethod=BANK_TRANSFER`, backend tự sinh `orderCode` dạng `DH83742`, đồng thời trả `transferContent`, `paymentQrUrl` hoặc `vietQrUrl` nếu admin đã cấu hình bank code + số tài khoản.

Ví dụ tạo đơn hàng thanh toán PayOS QR:

```json
{
  "customerName": "Nguyen Van A",
  "phone": "0900000000",
  "address": "Quan 1, TP.HCM",
  "paymentMethod": "PAYOS",
  "totalAmount": 2500000,
  "items": [
    {
      "productId": 1,
      "size": "M",
      "quantity": 1
    }
  ]
}
```

Nếu `paymentMethod=PAYOS`, backend gọi PayOS API tạo payment link, trả về các trường:
- `payosOrderCode` (number): mã đơn hàng trên PayOS
- `payosPaymentLinkId` (string): ID link thanh toán PayOS
- `payosCheckoutUrl` (string): URL chuyển hướng khách hàng đến trang thanh toán PayOS
- `payosQrCode` (string): URL ảnh QR code để quét thanh toán

Luồng thanh toán PayOS:
1. Khách chọn `PAYOS` → backend tạo link thanh toán trên PayOS
2. Frontend redirect khách đến `payosCheckoutUrl` hoặc hiển thị `payosQrCode`
3. Khách quét QR VietQR → thanh toán thành công
4. PayOS gửi webhook về `POST /api/webhook/payos`
5. Backend verify chữ ký, cập nhật đơn hàng thành `PAID`

## Admin API

### Media Cloudinary

- `POST /api/admin/media/upload` multipart form:
  - `file`: ảnh
  - `folder`: folder Cloudinary tự chỉ định, ví dụ `landingpage/banner`, `products/world-cup-2028`
  - `altText`: mô tả ảnh
- `GET /api/admin/media?folder=products`
- `DELETE /api/admin/media/{id}?deleteRemote=true`

### CMS

- `GET|PUT /api/admin/cms/hero`
- `GET|PUT /api/admin/cms/manifesto`
- `GET|POST /api/admin/cms/visual-narrative`
- `PUT|DELETE /api/admin/cms/visual-narrative/{id}`
- `GET|PUT /api/admin/cms/craftsmanship`
- `GET|PUT /api/admin/cms/footer`

### Chapters & Products

- `GET|POST /api/admin/chapters`
- `PUT|DELETE /api/admin/chapters/{id}`
- `GET|POST /api/admin/products`
- `PUT|DELETE /api/admin/products/{id}`
- `POST /api/admin/products/{id}/images` multipart form:
  - `files`: nhiều ảnh
  - `folder`: optional Cloudinary folder
- `DELETE /api/admin/products/{productId}/images/{imageId}`

Product request chính:

```json
{
  "slug": "ao-dau-world-cup-2028-san-nha",
  "name": "Áo Đấu World Cup 2028 - Sân Nhà",
  "tag": "Phiên bản Giới hạn",
  "price": 2500000,
  "description": "Mô tả sản phẩm",
  "material": "Polyester tái chế",
  "sizes": ["XS", "S", "M", "L", "XL"],
  "chapterId": 1,
  "sortOrder": 0,
  "active": true,
  "images": [
    {
      "imageUrl": "https://res.cloudinary.com/.../image.png",
      "imagePublicId": "products/world-cup/image",
      "altText": "Ảnh sản phẩm",
      "sortOrder": 0,
      "primaryImage": true
    }
  ]
}
```

### Orders & Payment

- `GET /api/admin/orders?status=PENDING_PAYMENT&paymentMethod=BANK_TRANSFER`
- `GET /api/admin/orders/{id}`
- `GET /api/admin/orders/code/{orderCode}`
- `PATCH /api/admin/orders/{id}/status`
- `GET|PUT /api/admin/payment/bank-transfer`
- `POST /api/admin/payment/bank-transfer/qr` multipart form `file`, optional `folder`

### Webhook (không cần xác thực)

- `POST /api/webhook/payos`: nhận kết quả thanh toán từ PayOS

Payload PayOS gửi về:

```json
{
  "code": "00",
  "desc": "success",
  "success": true,
  "data": {
    "orderCode": 123,
    "amount": 3000,
    "description": "VQRIO123",
    "accountNumber": "12345678",
    "reference": "TF230204212323",
    "transactionDateTime": "2023-02-04 18:25:00",
    "currency": "VND",
    "paymentLinkId": "124c33293c43417ab7879e14c8d9eb18",
    "code": "00",
    "desc": "Thành công"
  },
  "signature": "8d8640d802576397a1ce45ebda7f835055768ac7ad2e0bfb77f9b8f12cca4c7f"
}
```

Backend dùng `CHECKSUM_KEY` để verify chữ ký, nếu hợp lệ cập nhật đơn hàng thành `PAID`.

Trạng thái đơn hàng:

- `PENDING_PAYMENT`
- `PAYMENT_REVIEW`
- `PAID`
- `PROCESSING`
- `SHIPPING`
- `COMPLETED`
- `CANCELLED`

## Lưu ý PayOS Webhook

- Đăng ký webhook URL trong dashboard PayOS (my.payos.vn), ví dụ: `https://your-domain.com/api/webhook/payos`
- Endpoint `/api/webhook/**` không cần JWT, cho phép PayOS gọi về trực tiếp
- Backend dùng `PAYOS_CHECKSUM_KEY` để verify chữ ký HMAC-SHA256 trong webhook payload

