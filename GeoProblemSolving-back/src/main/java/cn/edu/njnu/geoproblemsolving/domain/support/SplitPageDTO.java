package cn.edu.njnu.geoproblemsolving.domain.support;

import lombok.Data;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.util.Assert;

/**
 * @ClassName SplitPageDTO
 * @Description todo
 * @Author sun_liber
 * @Date 2018/9/8
 * @Version 1.0.0
 */
@Data
public class SplitPageDTO {
    private Integer page = 0;
    private Integer pageSize = 10;
    private Boolean asc = false;
    private String value;

    public Pageable getPageable(String ... sortProperty){
        if(sortProperty.length==0){
            Sort sort= Sort.by(asc? Sort.Direction.ASC: Sort.Direction.DESC,"createTime");
            return PageRequest.of(page,pageSize,sort);
        }else{
            Sort sort= Sort.by(asc? Sort.Direction.ASC: Sort.Direction.DESC,sortProperty);
            return PageRequest.of(page,pageSize,sort);
        }
    }

    public Pageable getLatestPageable(int top){
        return getLatestPageable(top,"createTime");
    }

    public Pageable getLatestPageable(int top, String ... sortProperty){
        Assert.isTrue(top > 0, "Top number must not be less than 0");
        Assert.noNullElements(sortProperty, "Sort property must not be blank");
        return PageRequest.of(0, top, Sort.by(Sort.Direction.DESC, sortProperty));
    }


}
