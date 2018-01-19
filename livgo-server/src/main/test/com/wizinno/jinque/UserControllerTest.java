package com.wizinno.jinque;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Created by LiuMei on 2017-05-10.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration(value = "src/main/webapp")
@ContextConfiguration(locations = {"classpath*:/*.xml"})
@Ignore
public class UserControllerTest {
    @Autowired
    private WebApplicationContext wac;
    private MockMvc mockMvc;

    @Before
    public void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(wac).build();
    }

    @Test
    public void testAddUser() throws Exception {
        this.mockMvc
                .perform(post("/api/app/user")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("username", "001")
                        .param("password", "admin"))
                .andDo(print())
                .andExpect(status().isOk())
//                .andExpect(jsonPath("$", hasSize(1)))
//                .andExpect(jsonPath("$[0].field").value("orderNumber"))
//                .andExpect(jsonPath("$[0].actual").value("4955"))
//                .andExpect(jsonPath("$[0].expected").value("4956"))
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andExpect(content().json("{'reason':'用户已存在','code':5}"));
    }
}
