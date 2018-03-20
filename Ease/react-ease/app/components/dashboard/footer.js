import React, {Component} from "react";
import classnames from "classnames";
import {connect} from "react-redux";
import ReactCSSTransitionGroup from "react-addons-css-transition-group";


@connect(store => ({
  username: store.common.user.username
}))

class Footer extends Component {
  constructor(props){
    super(props);
    this.state = {
      view: 1,
      hideFooter: false,
      focus: false

    }
  }
  hideFooter = (e) => {
    this.setState({hideFooter: true})
  };

  showFooter = (e) => {
    this.setState({hideFooter: false});
    this.setState({focus: true});
  };

  hideFooterTimer = () => {
    setTimeout( () => {
      if (this.state.hideFooter === false && this.state.focus === false) {
        this.setState({hideFooter: true, view: 2});
      }
      this.setState({view: 2});
    }, 4000);
  };

  componentWillMount() {
    if (document.URL !== 'https://ease.space/#/main/dashboard/log') {
      this.hideFooterTimer();
      this.setState({view: 2})
    }
  }

  render(){
    return (
      <div class="footerMainContainer">
        <ReactCSSTransitionGroup
          transitionName="fade"
          transitionAppear={true}
          transitionEnter={true}
          transitionLeave={true}
          transitionAppearTimeout={1000}
          transitionEnterTimeout={300}
          transitionLeaveTimeout={300}>
        {!this.state.hideFooter &&
            <div class="footerContainer" onMouseLeave={this.hideFooter} onMouseEnter={this.showFooter}>
              <div class="footerLeftBox">
                <p>Daily background picture: <a href="https://unsplash.com">unsplash.com</a></p>
                <p>Credits for icons: <a href="https://semantic-ui.com">semantic-ui.com</a></p>
              </div>
              <div class="footerCenterBox">
                {this.state.view === 1 &&<p>Have good day {this.props.username}</p>}
                {this.state.view === 2 &&
                <p><a style={{textDecoration: "underline"}} href="https://ease.space/product">Product</a> - <a style={{textDecoration: "underline"}} href="https://ease.space/security">Security</a> - <a style={{textDecoration: "underline"}} href="https://ease.space/pricing">Team plans</a></p>
                }
              </div>
              <div class="footerRightBox">
                <div>
                  <p>Help us by giving a review!</p>
                </div>
              </div>
            </div>
        }
      </ReactCSSTransitionGroup>
        <div onMouseEnter={this.showFooter} class="showFooter">
        </div>
      </div>

    )
  }
}

export default Footer;