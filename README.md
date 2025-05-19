# Bài tập lớn OOP - Bomberman Game
Đây là phiên bản Java mô phỏng lại trò chơi [Bomberman](https://www.youtube.com/watch?v=mKIOVwqgSXM) kinh điển của NES.


## Mục lục
* [Giới thiệu](#giới-thiệu)
* [Mô tả về các đối tượng trong trò chơi](#mô-tả-về-các-đối-tượng-trong-trò-chơi)
* [Mô tả về các vật phẩm trong trò chơi](#mô-tả-về-các-vật-phẩm-trong-trò-chơi)
* [Mô tả về các kẻ địch trong trò chơi](#mô-tả-về-các-kẻ-địch-trong-trò-chơi)
* [Mô tả về gameplay, xử lý va chạm và xử lý bom nổ](#mô-tả-game-play-xử-lý-va-chạm-và-xử-lý-bom-nổ)


## Giới thiệu
Bomberman là một tựa game hành động giải đố cổ điển được phát triển bởi Hudson Soft. Ban đầu, game được phát hành cho hệ máy Famicom (tên gọi khác của NES tại Nhật Bản) vào ngày 19 tháng 12 năm 1985. Sau đó, game được phát hành tại thị trường Bắc Mỹ vào tháng 1 năm 1989.

Trong game, người chơi điều khiển nhân vật Bomberman, một robot đặt bom để tiêu diệt kẻ thù và phá hủy các khối gạch mềm nhằm tìm đường ra ở mỗi màn chơi. Bomberman bắt đầu với một quả bom duy nhất và bán kính nổ giới hạn. Tuy nhiên, người chơi có thể thu thập các vật phẩm tăng sức mạnh ẩn sau các khối gạch để tăng số lượng bom đặt được, tăng bán kính nổ, tăng tốc độ chạy.


## Mô tả về các đối tượng trong trò chơi
|                   Đồ họa                  |                                                                                                                                                                                       Chức năng                                                                                                                                                                                       |
|:-----------------------------------------:|:-------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------:|
|   ![Player](res/sprites/player_down.png)  |                                                                                                                                Nhân vật chính của trò chơi. Bomber có thể di chuyển theo 4 hướng trái/phải/lên/xuống theo sự điều khiển của người chơi                                                                                                                                |
| ![Enemy](res/sprites/balloom_right_1.png) |                                                                                            Các đối tượng mà Bomber phải tiêu diệt hết để có thể qua Level. Enemy có thể di chuyển ngẫu nhiên hoặc tự đuổi theo Bomber tùy theo loại Enemy. Các loại Enemy sẽ được mô tả cụ thể ở phần dưới                                                                                            |
|       ![Bomb](res/sprites/bomb.png)       | Đối tượng mà Bomber sẽ đặt và kích hoạt tại các ô Grass. Khi đã được kích hoạt, Bomber và Enemy không thể di chuyển vào vị trí Bomb. Tuy nhiên ngay khi Bomber vừa đặt và kích hoạt Bomb tại ví trí của mình, Bomber có một lần được đi từ vị trí đặt Bomb ra vị trí bên cạnh. Sau khi kích hoạt 2s, Bomb sẽ tự nổ, các đối tượng ![Flame](res/sprites/bomb_exploded.png) được tạo ra |
|      ![Grass](res/sprites/grass.png)      |                                                                                                                                            Đối tượng mà Bomber và Enemy có thể di chuyển xuyên qua, và cho phép đặt Bomb lên vị trí của nó                                                                                                                                            |
|       ![Wall](res/sprites/wall.png)       |                                                                                                                       Đối tượng cố định, không thể phá hủy bằng Bomb cũng như không thể đặt Bomb lên được, Bomber và Enemy không thể di chuyển vào đối tượng này                                                                                                                      |
|      ![Brick](res/sprites/brick.png)      |                                                                                       Đối tượng được đặt lên các ô Grass, không cho phép đặt Bomb lên nhưng có thể bị phá hủy bởi Bomb được đặt gần đó. Bomber và Enemy thông thường không thể di chuyển vào vị trí Brick khi nó chưa bị phá hủy                                                                                      |
|     ![Portal](res/sprites/portal.png)     |                                                                                    Đối tượng được giấu phía sau một đối tượng Brick. Khi Brick đó bị phá hủy, Portal sẽ hiện ra và nếu tất cả Enemy đã bị tiêu diệt thì người chơi có thể qua Level khác bằng cách di chuyển vào vị trí của Portal                                                                                    |


## Mô tả về các vật phẩm trong trò chơi
|                    Đồ họa                    |                                                                                             Chức năng                                                                                            |
|:--------------------------------------------:|:------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------:|
|  ![BombItem](res/sprites/powerup_bombs.png)  | Thông thường, nếu không có đối tượng Bomb nào đang trong trạng thái kích hoạt, Bomber sẽ được đặt và kích hoạt duy nhất một đối tượng Bomb. Item này giúp tăng số lượng Bomb có thể đặt thêm một |
| ![FlameItem](res/sprites/powerup_flames.png) |                                                          Item này giúp tăng phạm vi ảnh hưởng của Bomb khi nổ (độ dài các Flame lớn hơn)                                                         |
|  ![SpeedItem](res/sprites/powerup_speed.png) |                                                      Khi sử dụng Item này, Bomber sẽ được tăng vận tốc di chuyển thêm một giá trị thích hợp                                                      |


## Mô tả về các kẻ địch trong trò chơi
|                    Đồ họa                   |                                                      Chức năng                                                      |
|:-------------------------------------------:|:-------------------------------------------------------------------------------------------------------------------:|
| ![Balloom](res/sprites/balloom_right_1.png) |                            Enemy đơn giản nhất, di chuyển ngẫu nhiên với vận tốc cố định                            |
|    ![Oneal](res/sprites/oneal_right1.png)   | Tốc độ di chuyển thay đổi, lúc nhanh, lúc chậm và di chuyển "thông minh" hơn so với Balloom (biết đuổi theo Bomber) |


## Mô tả game play, xử lý va chạm và xử lý bom nổ
- Trong một màn chơi, Bomber sẽ được người chơi di chuyển, đặt và kích hoạt Bomb với mục tiêu chính là tiêu diệt tất cả Enemy và tìm ra vị trí Portal để có thể qua màn mới.
- Bomber sẽ bị giết khi va chạm với Enemy hoặc thuộc phạm vi Bomb nổ. Lúc đấy trò chơi kết thúc.
- Enemy bị tiêu diệt khi thuộc phạm vi Bomb nổ.
- Một đối tượng thuộc phạm vi Bomb nổ có nghĩa là đối tượng đó va chạm với một trong các tia lửa được tạo ra tại thời điểm một đối tượng Bomb nổ.
- Khi Bomb nổ, một Flame trung tâm tại vị trí Bomb nổ và bốn Flame tại bốn vị trí ô đơn vị xung quanh vị trí của Bomb xuất hiện theo bốn hướng trên/dưới/trái/phải. Độ dài bốn Flame xung quanh   mặc định là 1 đơn vị, được tăng lên khi Bomber sử dụng các FlameItem.
- Khi các Flame xuất hiện, nếu có một đối tượng thuộc loại Brick/Wall nằm trên vị trí một trong các Flame thì độ dài Flame đó sẽ được giảm đi để sao cho Flame chỉ xuất hiện đến vị trí đối tượng Brick/Wall theo hướng xuất hiện. Lúc đó chỉ có đối tượng Brick/Wall bị ảnh hưởng bởi Flame, các đối tượng tiếp theo không bị ảnh hưởng. Còn nếu vật cản Flame là một đối tượng Bomb khác thì đối tượng Bomb đó cũng sẽ nổ ngay lập tức.
