package be.vdab.personeel.security;

import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.jdbc.JdbcDaoImpl;

import javax.sql.DataSource;

@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
class SecurityConfig extends WebSecurityConfigurerAdapter {

     @Bean
    JdbcDaoImpl jdbcDaoImpl (DataSource dataSource){
        var impl = new JdbcDaoImpl();
        impl.setDataSource(dataSource);

        impl.setUsersByUsernameQuery("select email as username, paswoord as password, 1 as enabled from werknemers where email = ?");
        impl.setAuthoritiesByUsernameQuery(
                "select werknemers.email as username, jobtitels.naam as authorities " +
                "from werknemers inner join jobtitels " +
                "on werknemers.jobtitelid = jobtitels.id " +
                "where werknemers.email = ?");
        return impl;
     }

     @Override
    public void configure(WebSecurity web){
         web.ignoring()
                 .mvcMatchers("/img/**")
                 .mvcMatchers("/css/**");
     }

     @Override
    protected void configure(HttpSecurity http) throws Exception {
         http.formLogin(login -> login.loginPage("/login"));
         http.authorizeRequests(requests ->
                 requests.mvcMatchers("/", "/login", "/jobTitles/**").permitAll()
                         .mvcMatchers("/**").authenticated()
         );
         http.logout(logout -> logout.logoutSuccessUrl("/"));
     }
}
