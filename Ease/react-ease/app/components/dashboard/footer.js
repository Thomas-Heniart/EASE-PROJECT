import React, {Component} from "react";
import classnames from "classnames";
import {connect} from "react-redux";

@connect(store => ({
  username: store.common.user.username
}))

class Footer extends Component {
  constructor(props){
    super(props);
    this.state = {
      view: 1,
      hideFooter: false

    }
  }
  hideFooter = (e) => {
    this.setState({hideFooter: true})
  };

  showFooter = (e) => {
    this.setState({hideFooter: false});
  };

  hideFooterTimer = () => {
    setTimeout( () => {
      this.setState({hideFooter: true, view: 2});
    }, 4000);
  };

  componentWillMount() {
    this.hideFooterTimer();
  }

  render(){
    return (
      <div class="footerMainContainer">
        {!this.state.hideFooter
          ?
          <div class="footerContainer" onMouseLeave={this.hideFooter}>
            <div class="footerLeftBox">
               <p>Daily background picture: <a href="https://unsplash.com">unsplash.com</a></p>
               <p>Credits for icons: <a href="https://semantic-ui.com">semantic-ui.com</a></p>
            </div>
            <div class="footerCenterBox">
              {this.state.view === 1 && <p>Have good day {this.props.username}</p>}
              {this.state.view === 2 &&
              <p><a style={{textDecoration: "underline"}} href="https://ease.space/product">Product</a> - <a style={{textDecoration: "underline"}} href="https://ease.space/security">Security</a> - <a style={{textDecoration: "underline"}} href="https://ease.space/pricing">Team plans</a></p>
              }
            </div>
            <div class="footerRightBox">
              <div>
                <p>Help us by giving a review!</p>
              </div>
              <div>
                <p><a href="https://twitter.com/ease_space">Twitter</a> - <a href="https://chrome.google.com/webstore/detail/ease/hnacegpfmpknpdjmhdmpkmedplfcmdmp/reviews">Chrome store</a> - <a href="https://www.facebook.com/pg/YourEaseSpace/reviews/">Facebook</a></p>
              </div>
            </div>
          </div>
          :
          <div onMouseEnter={this.showFooter} class="showFooter">
          </div>
        }
      </div>
    )
  }
}

export default Footer;