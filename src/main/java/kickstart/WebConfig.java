package kickstart;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

	// this is made so the folder uploads can be accessed from the browser
	// to display the new submitted images for new Products
	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
		registry.addResourceHandler("/uploads/**") // URL pattern
			.addResourceLocations("file:uploads/"); // Physical directory
	}
}
