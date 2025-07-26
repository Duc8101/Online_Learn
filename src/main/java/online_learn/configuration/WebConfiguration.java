package online_learn.configuration;

import online_learn.interceptors.AuthenticationInterceptor;
import online_learn.interceptors.GuestOrStudentInterceptor;
import online_learn.interceptors.StudentInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfiguration implements WebMvcConfigurer {

    private final AuthenticationInterceptor authenticationInterceptor;
    private final StudentInterceptor studentInterceptor;
    private final GuestOrStudentInterceptor guestOrStudentInterceptor;

    public WebConfiguration(AuthenticationInterceptor authenticationInterceptor, StudentInterceptor studentInterceptor
            , GuestOrStudentInterceptor guestOrStudentInterceptor) {
        this.authenticationInterceptor = authenticationInterceptor;
        this.studentInterceptor = studentInterceptor;
        this.guestOrStudentInterceptor = guestOrStudentInterceptor;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(authenticationInterceptor)
                .addPathPatterns("/Profile", "/ChangePassword");
        registry.addInterceptor(studentInterceptor)
                .addPathPatterns("/MyCourse");
        registry.addInterceptor(guestOrStudentInterceptor)
                .addPathPatterns("/Courses/EnrollCourse", "/Courses/EnrollCourse/*");
    }
}
