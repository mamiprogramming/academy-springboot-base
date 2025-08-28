package com.spring.springbootapplication.dto;

import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class LearningItemForm {

    // hiddenで受け取る選択中の月
    @NotBlank
    private String learningMonth; // "YYYY-MM"

    // hiddenで受け取るカテゴリ（一覧のどの枠から来たか）
    @NotNull
    private Integer categoryId;    // ← Long から Integer に変更

    @NotBlank(message = "項目名は必ず入力してください")
    @Size(max = 50, message = "項目名は50文字以内で入力してください")
    private String item; // 項目名

    @NotNull(message = "学習時間は必ず入力してください")
    @PositiveOrZero(message = "学習時間は0以上の数字で入力してください")
    private Integer learningTime; // 分
}