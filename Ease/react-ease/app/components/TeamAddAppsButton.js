var React = require('react');
import {showAppAddUIElement} from "../actions/teamAppsAddUIActions"
import {connect} from "react-redux"
import { Header, Container, Menu, Segment, Popup, Checkbox, Form, Input,Divider, Icon, List, Select, Dropdown, Button, Grid, Message, Label,Transition } from 'semantic-ui-react';

@connect()
class TeamAddAppsButton extends React.Component {
  constructor(props){
    super(props);
    this.selectItem = this.selectItem.bind(this);
  }
  selectItem(type){
    this.props.dispatch(showAppAddUIElement(type, true));
  }
  render(){
    const targetType = this.props.target.purpose !== undefined ? 1 : 2;
    return (
        <Dropdown
            id="share_app_dropdown"
            size="mini"
            floating
            button
            icon={<i class="icon plus single" data-tip="Share accounts here" ref={(ref) => {window.refs.shareAppsButton = ref}}/>}>
          <Dropdown.Menu>
            {targetType === 2 &&
            <Dropdown.Item style={{whiteSpace:'inherit', lineHeight:'1.3'}}>
              Sending apps directly to one person will be available soon. <a href="mailto:victor@ease.space">Help us build this feature!</a>
            </Dropdown.Item>}
            {targetType === 1 &&
            <Dropdown.Item onClick={this.selectItem.bind(null,'Simple')}>
              <Icon name="square"/>
              Single app
              <Popup
                  size="mini"
                  style={{fontWeight: 'bold', textAlign:'center'}}
                  inverted
                  position="right center"
                  trigger={<Icon name='info' link className='right floated'/>}
                  content="Let multiple people have access to one account. Like your Twitter, Wordpress etc."/>
            </Dropdown.Item>}
            {targetType === 1 &&
            <Dropdown.Item onClick={this.selectItem.bind(null,'Multi')}>
              <Icon name="sitemap"/>
              Enterprise app
              <Popup
                  size="mini"
                  style={{fontWeight: 'bold', textAlign:'center'}}
                  inverted
                  position="right center"
                  trigger={<Icon name='info' link className='right floated'/>}
                  content="Share an account where each member uses his/her own ID and password to login to one website. Like Gmail, Trello, Slack... You can fill the credentials for your team members or let them do it."/>
            </Dropdown.Item>}
            {targetType === 1 &&
            <Dropdown.Item onClick={this.selectItem.bind(null, 'Link')}>
              <Icon name="linkify"/>
              Link app
              <Popup
                  size="mini"
                  style={{fontWeight: 'bold', textAlign:'center'}}
                  inverted
                  position="right center"
                  trigger={<Icon name='info' link className='right floated'/>}
                  content="Are you using tools without accounts? Need to share a blog or a source your team frequently uses? Here it is!"/>
            </Dropdown.Item>}
            {targetType === 1 &&
            <Dropdown.Item disabled className="align_items_center" style={{display:'flex'}}>
              <Icon name="disk outline"/>
              <span class="full_flex">Software credentials</span>
              <img style={{margin:0, height: '17px'}} src="/resources/images/Soon.png"/>
            </Dropdown.Item>}
            {targetType === 1 &&
            <Dropdown.Item disabled className="align_items_center" style={{display:'flex'}}>
              <Icon name="clock"/>
              <span class="full_flex">Temporary sharing</span>
              <img style={{margin:0, height: '17px'}} src="/resources/images/Soon.png"/>
            </Dropdown.Item>}
          </Dropdown.Menu>
        </Dropdown>
    )
  }
}

module.exports = TeamAddAppsButton;