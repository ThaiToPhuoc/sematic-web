import React  from "react";
import { Redirect, Route } from "react-router-dom";

function PrivateRoute({ component: Component, unique, ...restOfProps }) {
    let user = sessionStorage.getItem("user");
    try {
        user = JSON.parse(user);
    } catch { }
    const isAuthenticated = user?.token;

    return (
        <Route 
            {...restOfProps}
            render={(props) => 
                isAuthenticated 
                    ? (unique ? 
                    <Component {...props} key={props.match.params[unique]} /> :
                    <Component {...props} />)
                    : <Redirect to='/dang-nhap' />
            }
        />
    )
}

export default PrivateRoute;