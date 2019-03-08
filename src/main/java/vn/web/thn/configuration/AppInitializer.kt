package vn.web.thn.configuration

import org.springframework.web.servlet.support.AbstractAnnotationConfigDispatcherServletInitializer

class AppInitializer: AbstractAnnotationConfigDispatcherServletInitializer() {
    override fun getRootConfigClasses(): Array<Class<*>> {
        return arrayOf(HibernateConfig::class.java)
    }

    override fun getServletMappings(): Array<String> {
        return arrayOf("/")
    }

    override fun getServletConfigClasses(): Array<Class<*>> {
        return arrayOf(WebMvcConfig::class.java)
    }
}