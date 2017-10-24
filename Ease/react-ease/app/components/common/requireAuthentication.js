import React from 'react';
import {connect} from 'react-redux';
import {setLoginRedirectUrl} from "../../actions/commonActions";

export function requireAuthentication(Component){
  class AuthenticatedComponent extends React.Component {
    componentWillMount() {
      this.checkAuth();
    }
    componentWillReceiveProps(nextProps) {
      this.checkAuth();
    }
    checkAuth() {
      if (!this.props.authenticated) {
        this.props.dispatch(setLoginRedirectUrl(this.props.location.pathname));
        this.props.history.replace('/login');
      }
    }
    render() {
      return (
          <div>
            {this.props.authenticated === true
                ? <Component {...this.props}/>
                : null
            }
          </div>
      )

    }
  }

  const mapStateToProps = (state) => ({
    authenticated: state.common.authenticated
  });

  return connect(mapStateToProps)(AuthenticatedComponent)
}