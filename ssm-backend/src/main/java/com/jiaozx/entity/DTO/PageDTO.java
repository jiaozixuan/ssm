package com.jiaozx.entity.DTO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.domain.Sort;

/**
 * @ClassName PageDTO
 * @Description TODO
 * @Author @jiaozx
 * @Date 2022/8/4 19:47
 * @Version 1.0
 */

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class PageDTO {
    private int page = 0;
    private int size = 5;
    private Sort sort;
}
