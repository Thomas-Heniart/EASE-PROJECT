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
              <Popup
                  size="mini"
                  style={{fontWeight: 'bold', textAlign:'center'}}
                  position="right center"
                  trigger={
                    <Dropdown.Item onClick={this.selectItem.bind(null,'Simple')}>
                      <Icon name="square"/>
                      Single app
                    </Dropdown.Item>
                  }
                  content="Multiple people have access to one account (ex: twitter)."/>
            }
            {targetType === 1 &&
              <Popup
                  size="mini"
                  style={{fontWeight: 'bold', textAlign:'center'}}
                  position="right center"
                  trigger={
                    <Dropdown.Item onClick={this.selectItem.bind(null,'Multi')}>
                      <Icon name="sitemap"/>
                      Enterprise app
                    </Dropdown.Item>
                  }
                  content="Each person has a different account on one website (ex: Gmail, Trello)."/>
            }
            {targetType === 1 &&
              <Popup
                  size="mini"
                  style={{fontWeight: 'bold', textAlign:'center'}}
                  position="right center"
                  trigger={
                    <Dropdown.Item onClick={this.selectItem.bind(null, 'Link')}>
                      <Icon name="linkify"/>
                      Link app
                    </Dropdown.Item>
                  }
                  content="Are you using tools without accounts? Need to share a blog or a source your team frequently uses? Here it is!"/>
            }
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