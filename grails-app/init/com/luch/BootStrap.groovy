package com.luch

import com.luch.spring.security.SpringRole
import com.luch.spring.security.SpringUser
import com.luch.spring.security.SpringUserSpringRole
import grails.gorm.transactions.Transactional
import grails.util.Environment

class BootStrap {

    def init = { servletContext ->
        this.addTestUser();
    }

    def destroy = {
    }

    @Transactional
    void addTestUser() {
        SpringUser springUser, springUserSystem;
        SpringRole adminRole, roleGtg;
        SpringUserSpringRole springUserSpringRole;
        String username = 'DarthLuch@gmail.com';
        String adminRoleName = 'ROLE_ADMIN', roleGtgString = 'ROLE_GTG_GEN_USER';

        if (Environment.current == Environment.DEVELOPMENT) {

            springUserSystem = SpringUser.findByUsername('SYSTEM');
            if (!springUserSystem) {
                springUserSystem = new SpringUser(username: 'SYSTEM').save();
            }
            adminRole = SpringRole.findByAuthority(adminRoleName);
            if (!adminRole) {
                adminRole = new SpringRole(authority: adminRoleName, lastUpdatedBy: springUserSystem, createdBy: springUserSystem).save();
            }
            springUserSpringRole = SpringUserSpringRole.findBySpringUserAndSpringRole(springUserSystem, adminRole);
            if (!springUserSpringRole) {
                springUserSpringRole = new SpringUserSpringRole(springRole: adminRole, springUser: springUserSystem, lastUpdatedBy: springUserSystem, createdBy: springUserSystem).save();
            }
            roleGtg = SpringRole.findByAuthority(roleGtgString);
            if (!roleGtg) {
                roleGtg = new SpringRole(authority: roleGtgString, lastUpdatedBy: springUserSystem, createdBy: springUserSystem).save();
            }

            springUser = SpringUser.findByUsername(username);
            if (!springUser) {
                springUser = new SpringUser(username: username, password: 'trax', createdBy: springUserSystem, lastUpdatedBy: springUserSystem).save();
            }
            springUserSpringRole = SpringUserSpringRole.findBySpringUserAndSpringRole(springUser, adminRole);
            if (!springUserSpringRole) {
                springUserSpringRole = new SpringUserSpringRole(springRole: adminRole, springUser: springUser, lastUpdatedBy: springUserSystem, createdBy: springUserSystem).save();
            }

            springUser = SpringUser.findByUsername("Luch@Luch76.com");
            if (!springUser) {
                springUser = new SpringUser(username: "Luch@Luch76.com", password: 'trax', createdBy: springUserSystem, lastUpdatedBy: springUserSystem).save();
            }
            springUserSpringRole = SpringUserSpringRole.findBySpringUserAndSpringRole(springUser, roleGtg);
            if (!springUserSpringRole) {
                springUserSpringRole = new SpringUserSpringRole(springRole: roleGtg, springUser: springUser, lastUpdatedBy: springUserSystem, createdBy: springUserSystem).save();
            }
        }
    }
}
