import React from 'react';
import {Button, Icon, TextArea, Dropdown, Form, Menu, Message} from 'semantic-ui-react';

class PasteStep extends React.Component {
  render() {
    const {
      next,
      back,
      onChange,
      onChangeField,
      fields,
      pasted,
      passwordManager
    } = this.props;
    const separator = [
      {text: 'Comma (,)', value: ','},
      {text: 'Tab', value: '\t'},
      {text: 'Semicolon (;)', value: ';'},
      {text: 'Space', value: ' '}];
    const order = [
      {text: 'Name', value: 'name'},
      {text: 'URL', value: 'url'},
      {text: 'User ID', value: 'login'},
      {text: 'Password', value: 'password'},
      {text: 'Note', value: 'note'},
      {text: 'Tag', value: 'tag'},
      {text: '-', value: '-'}];
    return (
      <React.Fragment>
        <Form id='step3' error={this.props.error !== ''}>
          <p className='title'>Paste here the content of your file.</p>
          <TextArea name='paste' onChange={onChange} className={(passwordManager > 2 && passwordManager < 9) ? 'alone' : null} autoFocus/>
          {(passwordManager < 3 || passwordManager > 8) &&
          <React.Fragment>
            <p className='question'>1. How is your file structured?</p>
            <Menu className='menu_fields'>
              <Dropdown pointing className='link item' value={fields.field1} options={order} name='field1' onChange={onChangeField}/>
              <Dropdown pointing className='link item' value={fields.field2} options={order} name='field2' onChange={onChangeField}/>
              <Dropdown pointing className='link item' value={fields.field3} options={order} name='field3' onChange={onChangeField}/>
              <Dropdown pointing className='link item' value={fields.field4} options={order} name='field4' onChange={onChangeField}/>
              <Dropdown pointing className='link item' value={fields.field5} options={order} name='field5' onChange={onChangeField}/>
              <Dropdown pointing className='link item' value={fields.field6} options={order} name='field6' onChange={onChangeField}/>
            </Menu>
            <p>It should be as above if your file is ordered like: Linkedin, https://www.linkedin.com/, elon@spacex.com, ElonPassword75, personal account.</p>
            <div id='div_separator'>
              <p className='question'>2. How is the data separated in your file?</p>
              <Dropdown selection name='separator' defaultValue={','} options={separator} onChange={onChange}/>
            </div>
            <p>Ex: if your first row is "Website URL", "Login", "Password"; then choose Comma (,)</p>
          </React.Fragment>}
          <Message error content={this.props.error}/>
        </Form>
        <Button className={'left'} onClick={back}>
          <Icon name='arrow left'/> Back
        </Button>
        <Button className={'right'} positive onClick={next} disabled={!pasted}>
          Next <Icon name='arrow right'/>
        </Button>
      </React.Fragment>
    )
  }
}

export default PasteStep;