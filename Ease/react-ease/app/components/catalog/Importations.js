import React from 'react';
import {connect} from "react-redux";
import {handleSemanticInput} from "../../utils/utils";
import { Sticky, Rail, Input, List, Button, Icon, Grid, Image, Segment, Checkbox, Form } from 'semantic-ui-react';

class Importations extends React.Component {
  render() {
    return (
      <div id='importations'>
        <p className='title'>This feature is coming soon! 🚀</p>
        <p className='text'>
          We want to make this product perfect for <strong>your needs</strong>.<br/>
          Do you already have an idea of how you would like to import your passwords?<br/>
          If yes <a href="mailto:victor@ease.space"><u>send us a message</u></a>, if no you can still <a href="mailto:victor@ease.space"><u>send us a message</u></a>, its always a pleasure to get news from our users 😊
        </p>
      </div>
    )
  }
}

export default Importations;