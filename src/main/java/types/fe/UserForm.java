/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package types.fe;

import lombok.Data;

/**
 *
 * @author kutay.bezci
 */
@Data
public class UserForm {
    private String username;
    private String password;
    private String password2;
}
