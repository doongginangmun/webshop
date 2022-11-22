package com.toy.webshop.controlloer;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.toy.webshop.config.SecurityConfig;
import com.toy.webshop.dto.ItemDto;
import com.toy.webshop.entity.Address;
import com.toy.webshop.entity.Review;
import com.toy.webshop.entity.User;
import com.toy.webshop.entity.item.Book;
import com.toy.webshop.entity.item.Item;
import com.toy.webshop.form.ReviewForm;
import com.toy.webshop.service.ItemService;
import com.toy.webshop.service.ReviewService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import java.util.List;


import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.*;
@ExtendWith(SpringExtension.class)
@WebMvcTest(controllers = ReviewController.class,
        excludeFilters = {
                @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = SecurityConfig.class) })
class ReviewControllerTest {

    @Autowired
    ReviewController reviewController;
    @MockBean
    ReviewService reviewService;
    @MockBean
    ItemService itemService;
    @Autowired
    ObjectMapper objectMapper;

    private MockMvc mock;

    @BeforeEach
    public void setUp() {
        mock = standaloneSetup(reviewController)
                .setCustomArgumentResolvers(new PageableHandlerMethodArgumentResolver())
                .build();

    }

    @Test
    @DisplayName("review createForm")
    public void testcase() throws Exception {
        //given
        MockHttpServletRequestBuilder builder = MockMvcRequestBuilders.get("/review/{itemId}/new", 1L)
               ;
        mock.perform(builder)
                .andExpect(handler().handlerType(ReviewController.class))
                .andExpect(handler().methodName("createForm"))
                .andExpect(view().name("reviews/createReviewForm"))
                .andExpect(status().isOk())
                .andDo(MockMvcResultHandlers.print());
        ;
    }
    @Test
    @DisplayName("review create")
    public void reviewCreate() throws Exception {
        ReviewForm form = new ReviewForm(1L, 1L, "test용", 4.0f);
        ItemDto dto = new ItemDto(1L,"book1",1000,1,"작가", "isbn");
        Item item = new Book(1L,"book1",1000,1,"작가", "isbn");
        User user = new User("kdjkmit@naver.com","1234","김두한", new Address("1", "1","1"));
        Review review = Review.createReview(item, user, "좋네요", 4.0f);
        PageImpl page = new PageImpl(List.of(review));

        when(itemService.findBooks(anyLong())).thenReturn(dto);
        when(reviewService.findItemReviewAll(anyString(),any())).thenReturn(page);

        MockHttpServletRequestBuilder builder = MockMvcRequestBuilders.post("/review/new")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(form))
                ;

        mock.perform(builder)
                .andExpect(handler().handlerType(ReviewController.class))
                .andExpect(handler().methodName("registryReview"))
                .andDo(MockMvcResultHandlers.print());
    }
}