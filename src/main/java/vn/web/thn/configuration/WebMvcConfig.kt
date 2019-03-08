package vn.web.thn.configuration

import org.springframework.context.annotation.Configuration
import org.springframework.web.servlet.config.annotation.*
import org.springframework.web.servlet.view.JstlView
import org.springframework.web.servlet.view.InternalResourceViewResolver
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.ComponentScan
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry




@Configuration
@EnableWebMvc
@ComponentScan(basePackages = arrayOf("vn.web.thn.controller"))
open class WebMvcConfig: WebMvcConfigurer {
    @Bean
    open fun resolver(): InternalResourceViewResolver {
        val resolver = InternalResourceViewResolver()
        resolver.setViewClass(JstlView::class.java)
        resolver.setPrefix("/WEB-INF/views/")
        resolver.setSuffix(".jsp")
        return resolver
    }

    override fun addResourceHandlers(registry: ResourceHandlerRegistry?) {
        registry!!
                .addResourceHandler("/resources/**")
                .addResourceLocations("/resources/")
    }
}