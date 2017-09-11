var React = require('react');
var LoadingScreen = require('./LoadingScreen');
import {connect} from "react-redux";
import {withRouter} from "react-router-dom";
import {fetchNotifications} from "../../actions/notificationsActions";
import {fetchMyInformation} from "../../actions/commonActions";
import ReactTooltip from 'react-tooltip';
import WebsocketClient from './WebsocketClient';

@connect((store)=>{
  return {
    common: store.common
  };
})
class Base extends React.Component {
  constructor(props){
    super(props);
    this.state = {
      fetching : true
    };
    window.refs = {};
  }
  componentDidMount(){
    if (!this.props.common.authenticated){
      this.props.dispatch(fetchMyInformation()).then(response => {
        this.setState({fetching: false});
        if (this.props.common.authenticated)
          this.props.dispatch(fetchNotifications(0));
      });
    }else {
      this.setState({fetching: false});
      this.props.dispatch(fetchNotifications(0));
    }
  }
  render(){
    if (this.state.fetching)
      return (<LoadingScreen/>);
    else
      return (
          <div id="app-root">
            <ReactTooltip place="bottom"
                          type="dark"
                          globalEventOff="click"
                          effect="solid"
                          class="ease_tooltip"
                          multiline={true}
                          delayShow={300}/>
            <ReactTooltip effect="solid"
                          class="teams_tutorial_tooltip"
                          type="warning"
                          multiline={true}
                          place="bottom"
                          event="dblclick"
                          eventOff="dblclick"/>
            <WebsocketClient/>
            {this.props.children}
          </div>
      )
  }
}

module.exports = withRouter(Base);