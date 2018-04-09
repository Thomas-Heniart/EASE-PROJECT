import React, {Component} from "react";
import classnames from "classnames";
import queryString from "query-string";
import {showNewFeatureModal, showSimpleAppSettingsModal} from "../../actions/modalActions";
import Tutorial from "./Tutorial";
import DashboardColumn from "./DashboardColumn";
import {connect} from "react-redux";
import withScrolling from 'react-dnd-scrollzone';
const ScrollingComponent = withScrolling('div');
import ReactGA from 'react-ga';

@connect(store => ({
  dashboard: store.dashboard,
  user: store.common.user,
  background_picture: store.common.user.background_picture,
  tutorial_done: store.common.user.status.tuto_done
}))
class Dashboard extends Component {
  constructor(props){
    super(props);
    this.state = {
      scrolling: false,
      tutorial: false,
      ctrlOn: false
    }
  }
  onScroll = (e) => {
    if (this.state.scrolling && !e.target.scrollTop){
      this.setState({scrolling: false});
    } else if (!this.state.scrolling && !!e.target.scrollTop){
      this.setState({scrolling: true});
    }
  };
  componentDidUpdate(prevProps, prevState){
    if (prevProps.location.search !== this.props.location.search) {
      const query = queryString.parse(this.props.location.search);
      if (!!query.app_id && !!query.app_id.length) {
        const app = document.querySelector(`#app_${query.app_id} .logo_area`);
        this.props.history.replace(this.props.location.pathname);
        if (app){
          app.scrollIntoView(false);
          app.classList.add('ld');
          app.classList.add('ld-jump');
          setTimeout(() => {
            app.classList.remove('ld');
            app.classList.remove('ld-jump');
          }, 3000);
        }
      }
    }
    if (!this.props.user.new_feature_seen)
      this.props.dispatch(showNewFeatureModal({active: true}));
  }
  componentDidMount(){
    const query = queryString.parse(this.props.location.search);
    if (!!query.app_id && !!query.app_id.length) {
      const app = document.querySelector(`#app_${query.app_id} .logo_area`);
      this.props.history.replace(this.props.location.pathname);
      if (app){
        app.scrollIntoView(false);
        app.classList.add('ld');
        app.classList.add('ld-jump');
        setTimeout(() => {
          app.classList.remove('ld');
          app.classList.remove('ld-jump');
        }, 3000);
      }
    }
    if (!this.props.tutorial_done)
      this.setState({tutorial: true});
  }
  ctrlDownListener = (e) => {
    if (e.keyCode === 17 || e.keyCode === 91) {
      this.setState({ctrlOn: true});
    }
  };
  ctrlUpListener = (e) => {
    if (e.keyCode === 17 || e.keyCode === 91) {
      this.setState({ctrlOn: false});
    }
  };
  componentWillMount() {
    document.title = "Ease.space";
    ReactGA.pageview("main/dashboard");

    document.addEventListener('keydown', this.ctrlDownListener, true);
    document.addEventListener('keyup', this.ctrlUpListener, true);
  }
  componentWillUnmout(){
    document.removeEventListener('keydown', this.ctrlDownListener, true);
    document.removeEventListener('keyup', this.ctrlUpListener, true);
  }
  render(){
    const {columns} = this.props.dashboard;
    return (
        <div id="dashboard" class={classnames(this.props.background_picture ? 'ease-background' : null, this.state.scrolling ? 'scrolling': null)}>
          <ScrollingComponent onScroll={this.onScroll} class="ui container fluid full_flex display_flex">
            {columns.map((column,idx) => {
              return (
                  <DashboardColumn idx={idx} key={idx} profile_ids={column}/>
              )
            })}
            {this.state.tutorial &&
            <Tutorial/>}
          </ScrollingComponent>
          {this.state.ctrlOn &&
            <div id="ctrl_dashboard_info">
              Hold <strong>Cmd</strong> or <strong>Ctrl</strong> to login to multiple apps
            </div>}
        </div>
    )
  }
}

export default (Dashboard);