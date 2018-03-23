import React, {Component} from "react";
import {connect} from "react-redux";
import {setDashboardFooterState, welcomeMessageSeen} from "../../actions/dashboardActions";
import ReactCSSTransitionGroup from "react-addons-css-transition-group";


@connect(store => ({
  username: store.common.user.username,
  footerActive: store.dashboard.footerActive,
  welcomeMessageSeen: store.dashboard.welcomeMessageSeen
}))
class Footer extends Component {
  constructor(props){
    super(props);
    this.focus = false;
  }
  hideFooter = (e) => {
    this.props.dispatch(setDashboardFooterState({
      active: false
    }));
    this.focus = false;
  };
  showFooter = (e) => {
    this.props.dispatch(setDashboardFooterState(({
      active: true
    })));
    this.focus = true;
  };
  hideFooterTimer = () => {
    setTimeout( () => {
      if (this.props.footerActive && !this.focus)
        this.hideFooter();
      if (!this.props.welcomeMessageSeen)
        this.props.dispatch(welcomeMessageSeen());
    }, 4000);
  };
  componentDidMount() {
    this.hideFooterTimer();
  }
  render(){
    const {footerActive, welcomeMessageSeen} = this.props;

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
            {footerActive &&
            <div class="footerContainer" onMouseLeave={this.hideFooter} onMouseEnter={this.showFooter}>
              <div class="footerLeftBox">
                <p>Daily background picture: <a href="https://unsplash.com">unsplash.com</a></p>
                <p>Credits for icons: <a href="https://semantic-ui.com">semantic-ui.com</a></p>
              </div>
              <div class="footerCenterBox">
                {!welcomeMessageSeen &&<p style={{fontWeight:'bold'}}>Have a good day {this.props.username}!</p>}
                {welcomeMessageSeen &&
                <p><a style={{textDecoration: "underline"}} href="https://ease.space/product">Product</a> - <a style={{textDecoration: "underline"}} href="https://ease.space/security">Security</a> - <a style={{textDecoration: "underline"}} href="https://ease.space/pricing">Team plans</a></p>}
              </div>
              <div class="footerRightBox">
                <div>
                  <p style={{marginBottom: '5px'}}>Help us by giving a review!</p>
                  <span style={{color:'white'}}>
                    <a href="https://twitter.com/intent/tweet?text=%40ease_space%20%23thiseaselove" target="_blank">
                      Twitter
                    </a>
                    &nbsp;-&nbsp;
                    <a href="https://chrome.google.com/webstore/detail/ease/hnacegpfmpknpdjmhdmpkmedplfcmdmp/reviews" target="_blank">
                      Chrome store
                    </a>
                    &nbsp;-&nbsp;
                    <a href="https://www.facebook.com/pg/YourEaseSpace/reviews/" target="_blank">
                      Facebook
                    </a>
                  </span>
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