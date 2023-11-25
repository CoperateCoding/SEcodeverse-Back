package com.coperatecoding.secodeverseback.domain.board;

import com.coperatecoding.secodeverseback.domain.Comment;
import com.coperatecoding.secodeverseback.domain.User;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import net.minidev.json.annotate.JsonIgnore;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "board")
public class Board {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long pk;

    //사용자가 탈퇴했을 때 알수없음 처리를 위해 nullable
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_pk")
    @JsonIgnore
    private User user;

    @ManyToOne
    @JoinColumn(name = "category_pk")
    private BoardCategory category;

    @NotNull
    @CreationTimestamp
    @Column(name = "create_at")
    private LocalDateTime createAt = LocalDateTime.now(); // LocalDate, LocalDateTime 사용 시 @Temporal 생략 가능

    @CreationTimestamp
    @Column(name = "update_at")
    private LocalDateTime updateAt = LocalDateTime.now();

    @Column(name = "like_cnt")
    private Long likeCnt = 0L;

    @Column(name = "comment_cnt")
    private Long commentCnt = 0L;

    @NotNull
    @Column(length = 20)
    private String title;

    @NotNull
    @Column(length = 2000)
    private String content;

    @OneToMany(mappedBy = "board", cascade = CascadeType.ALL, orphanRemoval = true) //image 삭제하지 않아도 삭제됨
    private List<BoardImage> imageList = new ArrayList<>();

    @OneToMany(mappedBy = "board", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Comment> comments = new ArrayList<>();

    @OneToMany(mappedBy = "board", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Likes> likes = new ArrayList<>();

    public static Board makeBoard(User user, BoardCategory category, String title, String content) {
        Board board = new Board();
        board.user = user;
        board.category = category;
        board.title = title;
        board.content = content;
        return board;
    }

    public void edit(BoardCategory category, String title, String content){
        this.title = (title != null)? title : this.title;
        this.content = (content != null)? content : this.content;
        this.category = (category != null)? category : this.category;

        this.updateAt = LocalDateTime.now();
    }

    public void edit(BoardCategory category, String title, String content, List<BoardImage> imageList){
        this.title = (title != null)? title : this.title;
        this.content = (content != null)? content : this.content;
        this.category = (category != null)? category : this.category;

        if(imageList != null) {
            this.imageList.clear();
            this.imageList.addAll(imageList);
        }

        this.updateAt = LocalDateTime.now();
    }

    public void addLikeCnt(){
        this.likeCnt++;
    }

    public void deleteLikeCnt(){
        this.likeCnt--;
    }

    public void addCommentCnt(){
        this.commentCnt++;
    }

    public void deleteCommentCnt(){
        this.commentCnt--;
    }

    public String convertDate(LocalDateTime dateTime) {
        return dateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
    }

    public String convertPreviewDate(LocalDateTime updateAt) {
        return updateAt.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
    }


    public void setBoardImage(List<BoardImage> boardImageList) {
        this.imageList = boardImageList;
    }

    public String getPreview() {
        int previewLength = 10;
        if (content.length() <= previewLength) {
            return content;
        } else {
            return content.substring(0, previewLength) + "...";
        }
    }
}
